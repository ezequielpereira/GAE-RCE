# GAE-RCE
Google App Engine - Remote Code Execution bug ($36k bug bounty):
https://sites.google.com/site/testsitehacking/-36k-google-app-engine-rce

* app - Example application hosted in http://save-the-expanse.appspot.com/
  * /args - Returns all command-line arguments passed to the Java launcher
  * /nmap - Performs a scan of all ports in 169.254.169.253
  * /grpc - Runs the gRPC C++ client
* gRPC_client - Source code of the gRPC C++ client
* protos - All Protocol Buffer definitions extracted from Google App Engine
