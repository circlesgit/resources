"""Wrapper around git command-line tools."""

from __future__ import absolute_import, print_function

from lib.util import (
    CommonConfig,
    run_command,
)


class Git(object):
    """Wrapper around git command-line tools."""
    def __init__(self, args):
        """
        :type args: CommonConfig
        """
        self.args = args
        self.git = 'git'

    def get_diff_names(self, args):
        """
        :type args: list[str]
        :rtype: list[str]
        """
        cmd = ['diff', '--name-only', '--no-renames', '-z'] + args
        return self.run_git_split(cmd, '\0')

    def get_file_names(self, args):
        """
        :type args: list[str]
        :rtype: list[str]
        """
        cmd = ['ls-files', '-z'] + args
        return self.run_git_split(cmd, '\0')

    def get_branches(self):
        """
        :rtype: list[str]
        """
        cmd = ['for-each-ref', 'refs/heads/', '--format', '%(refname:strip=2)']
        return self.run_git_split(cmd)

    def get_branch(self):
        """
        :rtype: str
        """
        cmd = ['symbolic-ref', '--short', 'HEAD']
        return self.run_git(cmd).strip()

    def get_branch_fork_point(self, branch):
        """
        :type branch: str
        :rtype: str
        """
        cmd = ['merge-base', '--fork-point', branch]
        return self.run_git(cmd).strip()

    def run_git_split(self, cmd, separator=None):
        """
        :type cmd: list[str]
        :param separator: str | None
        :rtype: list[str]
        """
        output = self.run_git(cmd).strip(separator)

        if len(output) == 0:
            return []

        return output.split(separator)

    def run_git(self, cmd):
        """
        :type cmd: list[str]
        :rtype: str
        """
        return run_command(self.args, [self.git] + cmd, capture=True, always=True)[0]
