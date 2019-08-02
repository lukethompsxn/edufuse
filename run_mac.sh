#!/usr/bin/env bash
./clean.sh
/Applications/CMake.app/Contents/bin/cmake -DCMAKE_BUILD_TYPE=Debug .
make -j

DIRECTORY=/Users/lukethompson/Downloads/edufuse-testing2

if [ ! -d "$DIRECTORY" ]; then
  mkdir "$DIRECTORY"
fi

./bin/edufuse -d -s -f "$DIRECTORY"