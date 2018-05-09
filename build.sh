#!/usr/bin/env bash
# build.sh: Build the program
# Copyright (C) 2018  Fionn Langhans

# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.

# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.

# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.

! [ -d build ] && mkdir build
! [ -d build ] && (echo "Cannot create build dir" 1>&2 ; exit 1)

if ! javac -source 1.8 -cp src src/fionn/primenumberimage/*.java -d build -s build -h build -encoding UTF-8 ; then
    exit 1
fi

jar -cfe primenumberimage.jar feder.primenumberimage.ProgramMain -C build feder/primenumberimage/ProgramMain.class
