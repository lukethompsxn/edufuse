./clean.sh
cmake -DCMAKE_BUILD_TYPE=Debug .
make -j

DIRECTORY=/tmp/example

if [ ! -d "$DIRECTORY" ]; then
  mkdir /tmp/example
fi

./bin/edufuse -d -s -f /tmp/example