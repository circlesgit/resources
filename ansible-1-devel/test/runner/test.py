#!/usr/bin/env python
# PYTHON_ARGCOMPLETE_OK
"""Test runner for all Ansible tests."""

from __future__ import absolute_import, print_function

import errno
import os
import sys

from lib.util import (
    ApplicationError,
    display,
    raw_command,
)

from lib.delegation import (
    delegate,
)

from lib.executor import (
    command_posix_integration,
    command_network_integration,
    command_windows_integration,
    command_units,
    command_compile,
    command_sanity,
    command_shell,
    SANITY_TESTS,
    SUPPORTED_PYTHON_VERSIONS,
    COMPILE_PYTHON_VERSIONS,
    PosixIntegrationConfig,
    WindowsIntegrationConfig,
    NetworkIntegrationConfig,
    SanityConfig,
    UnitsConfig,
    CompileConfig,
    ShellConfig,
    ApplicationWarning,
    Delegate,
    generate_pip_install,
)

from lib.target import (
    find_target_completion,
    walk_posix_integration_targets,
    walk_network_integration_targets,
    walk_windows_integration_targets,
    walk_units_targets,
    walk_compile_targets,
    walk_sanity_targets,
)

import lib.cover


def main():
    """Main program function."""
    try:
        git_root = os.path.abspath(os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', '..'))
        os.chdir(git_root)
        args = parse_args()
        config = args.config(args)
        display.verbosity = config.verbosity
        display.color = config.color

        try:
            args.func(config)
        except Delegate as ex:
            delegate(config, ex.exclude, ex.require)

        display.review_warnings()
    except ApplicationWarning as ex:
        display.warning(str(ex))
        exit(0)
    except ApplicationError as ex:
        display.error(str(ex))
        exit(1)
    except KeyboardInterrupt:
        exit(2)
    except IOError as ex:
        if ex.errno == errno.EPIPE:
            exit(3)
        raise


def parse_args():
    """Parse command line arguments."""
    try:
        import argparse
    except ImportError:
        if '--requirements' not in sys.argv:
            raise
        raw_command(generate_pip_install('ansible-test'))
        import argparse

    try:
        import argcomplete
    except ImportError:
        argcomplete = None

    if argcomplete:
        epilog = 'Tab completion available using the "argcomplete" python package.'
    else:
        epilog = 'Install the "argcomplete" python package to enable tab completion.'

    parser = argparse.ArgumentParser(epilog=epilog)

    common = argparse.ArgumentParser(add_help=False)

    common.add_argument('-e', '--explain',
                        action='store_true',
                        help='explain commands that would be executed')

    common.add_argument('-v', '--verbose',
                        dest='verbosity',
                        action='count',
                        default=0,
                        help='display more output')

    common.add_argument('--color',
                        metavar='COLOR',
                        nargs='?',
                        help='generate color output: %(choices)s',
                        choices=('yes', 'no', 'auto'),
                        const='yes',
                        default='auto')

    test = argparse.ArgumentParser(add_help=False, parents=[common])

    test.add_argument('include',
                      metavar='TARGET',
                      nargs='*',
                      help='test the specified target').completer = complete_target

    test.add_argument('--exclude',
                      metavar='TARGET',
                      action='append',
                      help='exclude the specified target').completer = complete_target

    test.add_argument('--require',
                      metavar='TARGET',
                      action='append',
                      help='require the specified target').completer = complete_target

    test.add_argument('--coverage',
                      action='store_true',
                      help='analyze code coverage when running tests')

    add_changes(test, argparse)
    add_environments(test)

    integration = argparse.ArgumentParser(add_help=False, parents=[test])

    integration.add_argument('--python',
                             metavar='VERSION',
                             choices=SUPPORTED_PYTHON_VERSIONS,
                             help='python version: %s' % ', '.join(SUPPORTED_PYTHON_VERSIONS))

    integration.add_argument('--start-at',
                             metavar='TARGET',
                             help='start at the specified target').completer = complete_target

    integration.add_argument('--start-at-task',
                             metavar='TASK',
                             help='start at the specified task')

    integration.add_argument('--allow-destructive',
                             action='store_true',
                             help='allow destructive tests (--local and --tox only)')

    integration.add_argument('--retry-on-error',
                             action='store_true',
                             help='retry failed test with increased verbosity')

    subparsers = parser.add_subparsers(metavar='COMMAND')
    subparsers.required = True  # work-around for python 3 bug which makes subparsers optional

    posix_integration = subparsers.add_parser('integration',
                                              parents=[integration],
                                              help='posix integration tests')

    posix_integration.set_defaults(func=command_posix_integration,
                                   targets=walk_posix_integration_targets,
                                   config=PosixIntegrationConfig)

    add_extra_docker_options(posix_integration)

    network_integration = subparsers.add_parser('network-integration',
                                                parents=[integration],
                                                help='network integration tests')

    network_integration.set_defaults(func=command_network_integration,
                                     targets=walk_network_integration_targets,
                                     config=NetworkIntegrationConfig)

    windows_integration = subparsers.add_parser('windows-integration',
                                                parents=[integration],
                                                help='windows integration tests')

    windows_integration.set_defaults(func=command_windows_integration,
                                     targets=walk_windows_integration_targets,
                                     config=WindowsIntegrationConfig)

    windows_integration.add_argument('--windows',
                                     metavar='VERSION',
                                     action='append',
                                     help='windows version')

    units = subparsers.add_parser('units',
                                  parents=[test],
                                  help='unit tests')

    units.set_defaults(func=command_units,
                       targets=walk_units_targets,
                       config=UnitsConfig)

    units.add_argument('--python',
                       metavar='VERSION',
                       choices=SUPPORTED_PYTHON_VERSIONS,
                       help='python version: %s' % ', '.join(SUPPORTED_PYTHON_VERSIONS))

    units.add_argument('--collect-only',
                       action='store_true',
                       help='collect tests but do not execute them')

    compiler = subparsers.add_parser('compile',
                                     parents=[test],
                                     help='compile tests')

    compiler.set_defaults(func=command_compile,
                          targets=walk_compile_targets,
                          config=CompileConfig)

    compiler.add_argument('--python',
                          metavar='VERSION',
                          choices=COMPILE_PYTHON_VERSIONS,
                          help='python version: %s' % ', '.join(COMPILE_PYTHON_VERSIONS))

    sanity = subparsers.add_parser('sanity',
                                   parents=[test],
                                   help='sanity tests')

    sanity.set_defaults(func=command_sanity,
                        targets=walk_sanity_targets,
                        config=SanityConfig)

    sanity.add_argument('--test',
                        metavar='TEST',
                        action='append',
                        choices=[t.name for t in SANITY_TESTS],
                        help='tests to run')

    sanity.add_argument('--skip-test',
                        metavar='TEST',
                        action='append',
                        choices=[t.name for t in SANITY_TESTS],
                        help='tests to skip')

    sanity.add_argument('--list-tests',
                        action='store_true',
                        help='list available tests')

    sanity.add_argument('--python',
                        metavar='VERSION',
                        choices=SUPPORTED_PYTHON_VERSIONS,
                        help='python version: %s' % ', '.join(SUPPORTED_PYTHON_VERSIONS))

    shell = subparsers.add_parser('shell',
                                  parents=[common],
                                  help='open an interactive shell')

    shell.set_defaults(func=command_shell,
                       config=ShellConfig)

    add_environments(shell, tox_version=True)
    add_extra_docker_options(shell)

    coverage_common = argparse.ArgumentParser(add_help=False, parents=[common])

    add_environments(coverage_common, tox_version=True, tox_only=True)

    coverage = subparsers.add_parser('coverage',
                                     help='code coverage management and reporting')

    coverage_subparsers = coverage.add_subparsers(metavar='COMMAND')
    coverage_subparsers.required = True  # work-around for python 3 bug which makes subparsers optional

    coverage_combine = coverage_subparsers.add_parser('combine',
                                                      parents=[coverage_common],
                                                      help='combine coverage data and rewrite remote paths')

    coverage_combine.set_defaults(func=lib.cover.command_coverage_combine,
                                  config=lib.cover.CoverageConfig)

    coverage_erase = coverage_subparsers.add_parser('erase',
                                                    parents=[coverage_common],
                                                    help='erase coverage data files')

    coverage_erase.set_defaults(func=lib.cover.command_coverage_erase,
                                config=lib.cover.CoverageConfig)

    coverage_report = coverage_subparsers.add_parser('report',
                                                     parents=[coverage_common],
                                                     help='generate console coverage report')

    coverage_report.set_defaults(func=lib.cover.command_coverage_report,
                                 config=lib.cover.CoverageConfig)

    coverage_html = coverage_subparsers.add_parser('html',
                                                   parents=[coverage_common],
                                                   help='generate html coverage report')

    coverage_html.set_defaults(func=lib.cover.command_coverage_html,
                               config=lib.cover.CoverageConfig)

    coverage_xml = coverage_subparsers.add_parser('xml',
                                                  parents=[coverage_common],
                                                  help='generate xml coverage report')

    coverage_xml.set_defaults(func=lib.cover.command_coverage_xml,
                              config=lib.cover.CoverageConfig)

    if argcomplete:
        argcomplete.autocomplete(parser, always_complete_options=False, validator=lambda i, k: True)

    args = parser.parse_args()

    if args.explain and not args.verbosity:
        args.verbosity = 1

    if args.color == 'yes':
        args.color = True
    elif args.color == 'no':
        args.color = False
    else:
        args.color = sys.stdout.isatty()

    return args


def add_changes(parser, argparse):
    """
    :type parser: argparse.ArgumentParser
    :type argparse: argparse
    """
    parser.add_argument('--changed', action='store_true', help='limit targets based on changes')

    changes = parser.add_argument_group(title='change detection arguments')

    changes.add_argument('--tracked', action='store_true', help=argparse.SUPPRESS)
    changes.add_argument('--untracked', action='store_true', help='include untracked files')
    changes.add_argument('--ignore-committed', dest='committed', action='store_false', help='exclude committed files')
    changes.add_argument('--ignore-staged', dest='staged', action='store_false', help='exclude staged files')
    changes.add_argument('--ignore-unstaged', dest='unstaged', action='store_false', help='exclude unstaged files')

    changes.add_argument('--changed-from', metavar='PATH', help=argparse.SUPPRESS)
    changes.add_argument('--changed-path', metavar='PATH', action='append', help=argparse.SUPPRESS)


def add_environments(parser, tox_version=False, tox_only=False):
    """
    :type parser: argparse.ArgumentParser
    :type tox_version: bool
    :type tox_only: bool
    """
    parser.add_argument('--requirements',
                        action='store_true',
                        help='install command requirements')

    environments = parser.add_mutually_exclusive_group()

    environments.add_argument('--local',
                              action='store_true',
                              help='run from the local environment')

    if tox_version:
        environments.add_argument('--tox',
                                  metavar='VERSION',
                                  nargs='?',
                                  default=None,
                                  const='.'.join(str(i) for i in sys.version_info[:2]),
                                  choices=SUPPORTED_PYTHON_VERSIONS,
                                  help='run from a tox virtualenv: %s' % ', '.join(SUPPORTED_PYTHON_VERSIONS))
    else:
        environments.add_argument('--tox',
                                  action='store_true',
                                  help='run from a tox virtualenv')

    if tox_only:
        environments.set_defaults(
            docker=None,
            remote=None,
            remote_stage=None,
        )

        return

    environments.add_argument('--docker',
                              metavar='IMAGE',
                              nargs='?',
                              default=None,
                              const='ubuntu1604',
                              help='run from a docker container')

    environments.add_argument('--remote',
                              metavar='PLATFORM',
                              default=None,
                              help='run from a remote instance')

    remote = parser.add_argument_group(title='remote arguments')

    remote.add_argument('--remote-stage',
                        metavar='STAGE',
                        help='remote stage to use: %(choices)s',
                        choices=['prod', 'dev'],
                        default='prod')


def add_extra_docker_options(parser):
    """
    :type parser: argparse.ArgumentParser
    """
    docker = parser.add_argument_group(title='docker arguments')

    docker.add_argument('--docker-util',
                        metavar='IMAGE',
                        default='httptester',
                        help='docker utility image to provide test services')

    docker.add_argument('--docker-privileged',
                        action='store_true',
                        help='run docker container in privileged mode')


def complete_target(prefix, parsed_args, **_):
    """
    :type prefix: unicode
    :type parsed_args: any
    :rtype: list[str]
    """
    return find_target_completion(parsed_args.targets, prefix)


if __name__ == '__main__':
    main()
