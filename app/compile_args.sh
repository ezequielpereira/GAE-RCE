#!/bin/bash

cd src/main/java
javah -jni Args
JAVA_INCLUDE=$(dirname $(dirname $(readlink -f $(which javac))))/include # https://stackoverflow.com/a/20653441
g++ -s -fPIC -nostdlib -I $JAVA_INCLUDE -I $JAVA_INCLUDE/linux -o libhack.so -shared Args.cpp
mv libhack.so ../webapp/WEB-INF
