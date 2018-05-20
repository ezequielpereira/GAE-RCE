#include <iostream>
#include <grpc++/grpc++.h>
#include "apphosting/base/runtime.pb.h"
#include "apphosting/base/runtime.grpc.pb.h"

int main(int argc, char** argv) {
  if (argc < 6) {
    std::cerr << "Missing arguments" << std::endl;
    std::cerr << "Usage: ./client [API package] [Call] [PB] [Security ticket] [Set PB]" << std::endl;
    return 1;
  }

  auto creds = grpc::InsecureChannelCredentials();

  grpc::ChannelArguments ch_args;
  ch_args.SetMaxReceiveMessageSize(-1);

  auto channel = grpc::CreateCustomChannel("169.254.169.253:4", creds, ch_args);

  auto stub = apphosting::APIHost::NewStub(channel);

  apphosting::APIRequest request;
  request.set_api_package(argv[1]);
  request.set_call(argv[2]);
  if (argv[5][0] == '1') request.set_pb(argv[3]);
  request.set_request_encoding(apphosting::APIRequest_Encoding_JSON);
  request.set_response_encoding(apphosting::APIRequest_Encoding_JSON);
  request.set_security_ticket(argv[4]);

  apphosting::APIResponse reply;

  grpc::ClientContext context;

  grpc::Status status = stub->Call(&context, request, &reply);

  std::cout << std::endl;
  if (status.ok()) {
    std::cout << reply.DebugString() << std::endl;
  } else {
    std::cout << status.error_code() << ": " << status.error_message() << std::endl;
    return -1;
  }

  return 0;
}
