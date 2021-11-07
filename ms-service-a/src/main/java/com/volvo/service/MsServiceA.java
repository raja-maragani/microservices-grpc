package com.volvo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class MsServiceA extends MsServiceAGrpc.MsServiceAImplBase{

    final Logger log = LoggerFactory.getLogger(MsServiceA.class);
    
    @Override
    public void helloWorld(InputA request, StreamObserver<OutputA> responseObserver) {
        log.info("MsServiceA request: helloWorld started");
        responseObserver.onNext(OutputA.newBuilder().setQuery("Service A Hello-World").build());
        responseObserver.onCompleted();
        log.info("MsServiceA request: helloWorld completed");
    }
}
