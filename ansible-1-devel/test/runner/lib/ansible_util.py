"""Miscellaneous utility functions and classes specific to ansible cli tools."""

from __future__ import absolute_import, print_function

import os

from lib.util import common_environment


def ansible_environment(args):
    """
    :type args: CommonConfig
    :rtype: dict[str, str]
    """
    env = common_environment()
    path = env['PATH']

    ansible_path = os.path.join(os.getcwd(), 'bin')

    if not path.startswith(ansible_path + os.pathsep):
        path = ansible_path + os.pathsep + path

    ansible = dict(
        ANSIBLE_FORCE_COLOR='%s' % 'true' if args.color else 'false',
        ANSIBLE_DEPRECATION_WARNINGS='false',
        ANSIBLE_CONFIG='/dev/null',
        PYTHONPATH=os.path.abspath('lib'),
        PAGER='/bin/cat',
        PATH=path,
    )

    env.update(ansible)

    return env
