# -*- coding: utf-8 -*-
#
# Copyright (C) 2015 Matt Martz <matt@sivel.net>
# Copyright (C) 2015 Rackspace US, Inc.
#
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.

import ast
import sys

# We only use StringIO, since we cannot setattr on cStringIO
from StringIO import StringIO


def find_globals(g, tree):
    """Uses AST to find globals in an ast tree"""
    for child in tree:
        if hasattr(child, 'body') and isinstance(child.body, list):
            find_globals(g, child.body)
        elif isinstance(child, (ast.FunctionDef, ast.ClassDef)):
            g.add(child.name)
            continue
        elif isinstance(child, ast.Assign):
            try:
                g.add(child.targets[0].id)
            except (IndexError, AttributeError):
                pass
        elif isinstance(child, ast.Import):
            g.add(child.names[0].name)
        elif isinstance(child, ast.ImportFrom):
            for name in child.names:
                g_name = name.asname or name.name
                if g_name == '*':
                    continue
                g.add(g_name)


class CaptureStd():
    """Context manager to handle capturing stderr and stdout"""

    def __enter__(self):
        self.sys_stdout = sys.stdout
        self.sys_stderr = sys.stderr
        sys.stdout = self.stdout = StringIO()
        sys.stderr = self.stderr = StringIO()
        setattr(sys.stdout, 'encoding', self.sys_stdout.encoding)
        setattr(sys.stderr, 'encoding', self.sys_stderr.encoding)
        return self

    def __exit__(self, exc_type, exc_value, traceback):
        sys.stdout = self.sys_stdout
        sys.stderr = self.sys_stderr

    def get(self):
        """Return ``(stdout, stderr)``"""

        return self.stdout.getvalue(), self.stderr.getvalue()
