# microservices-grpc

microservices-grpc is a Microservice architecture-based application. It has two spring-boot service applications 

 - Service A (ms-service-a)  
 - Service B(ms-service-b)
 
 The above two microservices are accessed through a gateway application(ms-gateway). 
 The gateway communicated with the microservices used by the gRPC.
 The Gateway application endpoint is implemented using GraphQL.
 microservices-grpc's microservices generate application logs and those are visible through the Kibana.
 

# Application connectivity

![enter image description here](https://www.integrify.com/site/assets/files/2636/process-flow.png)

## Build and Run
### Prerequsit
Docker
Java 8 +
Maven
### Clone application 
git clone https://github.com/raja-maragani/microservices-grpc.git

### Build application 
Run the below maven command on the microservices-grpc
**mvn clean install**
### Docker images
Dockerfiles are available in the below 3 applications 
 - ms-gateway
 - ms-service-a 
 - ms-service-b

SmartyPants converts ASCII punctuation characters into "smart" typographic punctuation HTML entities. For example:

|        Application         |Dockerfile Path                          |Docker command                         |
|----------------|-------------------------------|-----------------------------|
|ms-gateway|cd ms-gateway            |docker build --tag=ms-gateway:latest .            |
|ms-service-a          |cd ms-service-a            |docker build --tag=ms-service-a:latest .            |
|ms-service-b          |cd ms-service-b |docker build --tag=ms-service-b:latest .|


Which will create the docker latest image for our application

### docker-compose up
Run the **docker-compose up** on the directory microservices-grpc
The docker-compose.yml will up the below applications
|        Application         |Port                          |Version                         |
|----------------|-------------------------------|-----------------------------|
|ms-gateway|8080            |latest (0.0.1-SNAPSHOT)            |
|ms-service-a          |6565 (gRPC communication port)           |latest (0.0.1-SNAPSHOT)            |
|ms-service-b          |6566 (gRPC communication port) |latest (0.0.1-SNAPSHOT)|
|elasticsearch          |9200  |7.2.0|
|logstash          |5044 |latest 7.2.0|
|kibana          |5601 |latest 7.2.0|

## GraphQL
The **GatewayGraphQLService** loads the greetings.graphql schema. We implemented the two queries.

    schema {
     query: Query
    }
    
    type Query {
     helloWorldSA: Greetings
     helloWorldSB: Greetings
    }
    
    type Greetings {
      greetings: String
    }

|        Endpoint         |Input                          |Method type |Status | Output|  Comment| 
|----------------|-------------------------------|-----------------------------|-----------------------------|-----------------------------|-----------------------------|
|localhost:8080/api|{ helloWorldSA { greetings } }            |POST           |200           |{helloWorldSA={greetings=Hello-World! from Service A}}           |Service A and Servive B up and running           |
|localhost:8080/api|{ helloWorldSB { greetings } }            |POST           |200           |{helloWorldSB={greetings=Hello-World! from Service B}}           |Service A and Servive B up and running           |
|localhost:8080/api|{ helloWorldSC { greetings } }            |POST           |400           |Bad Service Request.           |Service C is not there           |
|localhost:8080/api|{ helloWorldSA { greetings } }            |POST           |500           |Service Down. Please try after some time           |Service A - not running           |

## Logger format

Application is generating the loggers in JSON format and the reason is ELK is implemented on top of our application.
the application-generated logs will process by Logstash and push to Elastic Search and the end-user can monitor those logs using Kibana.

    {
       "@timestamp":"2021-11-08T16:58:09.110Z",
       "@version":"1",
       "level":"INFO",
       "message":"MsServiceB request: helloWorld started",
       "logger_name":"com.volvo.service.MsServiceB",
       "thread_name":"grpc-default-executor-0"
    }

>This JSON we can be improved based on our requirement, 
To improve the log change the logback.xml file of the application.
## Exception Handling
The **MsGatewayExceptionHandler** will handle the application throw exceptions. and return the request as a bad request.
> This can be improved based on our requirements.


## How GraphQL is working
The GraphQL runtime wiring logic is placed in **GatewayGraphQLService**.
This GatewayGraphQLService will do the runtime wiring(Map to the data fetcher) for our endpoints. 
We implemented two queries and these two queries are mapped with Greetings data fetcher.
helloWorldSA --> HwSaGreetingsFetcher
helloWorldSB --> HwSbGreetingsFetcher


# How gRPC working


gRPC is a **modern, open-source remote procedure call (RPC) framework that can run anywhere**. 
It enables client and server applications to communicate transparently and makes it easier to build connected systems.

application.yaml file maintain the grpc configuration:

    grpc:
      client:
        ms-service-a:
          address: static://ms-service-a:6565
          negotiationType: plaintext
        ms-service-b:
          address: static://ms-service-b:6566
          negotiationType: plaintext

Dependencies:

      <dependency>
           <groupId>io.grpc</groupId>
           <artifactId>grpc-protobuf</artifactId>
           <version>1.30.2</version>
       </dependency>
       <dependency>
           <groupId>io.grpc</groupId>
           <artifactId>grpc-stub</artifactId>
           <version>1.30.2</version>
       </dependency>


**Followed the below steps**
1.  Our application defined two services in  _.proto_ files (servicea.proto and serviceb.proto)
2.  Generated server and client code using the protocol buffer compiler
3.  Created the server application, implemented the generated service interfaces, and spawning the gRPC server
4.  Created the client application, making RPC calls using generated stubs

**servicea.proto**

    syntax = "proto3";
    package service;
    option java_package = "com.volvo.service";
    option java_multiple_files = true;
    message OutputA {
      string query = 1;
    }
    message InputA {
    }
    service MsServiceA {
      rpc helloWorld(InputA) returns (stream OutputA) {};
    }

# Kibana logs 
Kibana connecting to the elasticsearch index to fetch the logger information.
Our scenario Kibana discovery the **logstash-*** for fetch the logs.