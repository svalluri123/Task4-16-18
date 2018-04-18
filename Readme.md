Custom Proxy Implementation using Little proxy library

proxy-Filter

FilteredProxyServer

LittleProxy library is used for FilteredProxyServer Implementation
Postman is used as test client to send the HTTP requests to the FilteredProxyServer
SpringBoot application is the destination server which accepts the HTTP requests from the FilteredProxy server and sends response back to FilteredProxy server

FilteredProxy server checks the HTTP request headers for content-type
* If content-type is xml then it will forward the request to the destination server
* else it will return the message to the client saying Invalid content type.

Using DefaultProxyServer, a primary implementation of HTTPProxysever of little proxy library, server is bootstrapped on localhost with configurable port. HTTPFilter source is a factory 
for HTTP filters, which is having various methods to filter the HTTPObjects.

There are different methods to monitor the HTTP request life cycle.

In ProxyToServerRequest method, FilteredProxy server checks the Content-type of the incoming request for xml, proxyToServer request method returns null if it forwards the request to
the destination server, else it returns HTTPResponse back to client if error.

In proxyToServerResolutionStarted method does DNS resolution from proxy to destination server, it returns null if resolution continue, this method returns configurable destination 
hostname along with port for the proxy to connect

serverToProxyResponse returns the HttpObject to the Proxy server

proxyToClientResponse returns the same HTTPObject from destination server to the client.

SpringBoot Application is the destination host which is used to accept the HTTP request from the FilteredProxy server

Server class is annotated with @SpringBootApplication which itself gives runtime environment with embedded webserver. 

Greeting class is a simple POJO with two fields such as id and content

Greeting controller with two resources /greeting and /greeting1 returns id which gets incremented every-time and data in the content as HTTP response to the proxy server which in turn
sends response to the client.

