#!/bin/bash

# I don't know how to write a Makefile

mkdir -p cpp
for proto_file in $(find protos -name "*.proto")
do
  echo -e "\e[1;33mConverting $proto_file to C++ code\e[0m"
  protoc -I protos --cpp_out=cpp $proto_file
  protoc -I protos --grpc_out=cpp --plugin=protoc-gen-grpc=`which grpc_cpp_plugin` $proto_file
done

for cpp_file in $(find cpp -name "*.cc")
do
  echo -e "\e[1;33mCompiling $cpp_file\e[0m"
  out_file=cpp_compiled/$(echo $cpp_file | cut -d "/" -f 2-).o
  out_dir=$(dirname $out_file)
  mkdir -p $out_dir
  g++ -I cpp -std=c++11 `pkg-config --cflags protobuf grpc` -c -o $out_file $cpp_file
done

echo -e "\e[1;32mCompiling the client\e[0m"
g++ -I cpp -std=c++11 `pkg-config --cflags protobuf grpc` -c -o gRPC_client.o gRPC_client.cc
g++ -static -s -O3 $(find cpp_compiled -name "*.o") gRPC_client.o -L/usr/local/lib `pkg-config --libs protobuf grpc++ grpc openssl zlib` -Wl,--no-as-needed -lgrpc++_reflection -Wl,--as-needed -ldl -o gRPC_client
