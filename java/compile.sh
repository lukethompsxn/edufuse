#!/usr/bin/env bash
javac -h . src/main/java/util/FUSELink.java
gcc -shared -fpic -o libFUSELink.so -I/usr/lib/jvm/java-8-openjdk-amd64/include -I/usr/lib/jvm/java-8-openjdk-amd64/include/linux util_FUSELink.c
java  -Djava.library.path=. FUSELink
