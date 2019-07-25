#!/usr/bin/env bash
./clean.sh
/Applications/CMake.app/Contents/bin/cmake -DCMAKE_BUILD_TYPE=Debug .
make -j

DIRECTORY=/Users/lukethompson/Downloads/fusetesting

if [ ! -d "$DIRECTORY" ]; then
  mkdir /tmp/example
fi

./bin/edufuse -d -s -f /Users/lukethompson/Downloads/fusetesting