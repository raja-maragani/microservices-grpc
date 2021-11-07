package com.volvo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class MsServiceB extends MsServiceBGrpc.MsServiceBImplBase{

    final Logger log = LoggerFactory.getLogger(MsServiceB.class);
    
    @Override
    public void helloWorld(InputB request, StreamObserver<OutputB> responseObserver) {
        log.info("MsServiceB request: helloWorld started");
        responseObserver.onNext(OutputB.newBuilder().setQuery("Service B Hello-World").build());
        responseObserver.onCompleted();
        log.info("MsServiceB request: helloWorld completed");
    }
}
