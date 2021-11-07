package com.volvo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.volvo.controller.GatewayController;

import net.devh.boot.grpc.client.inject.GrpcClient;
import reactor.core.publisher.Flux;

@Service
public class GatewayService {

    @GrpcClient("ms-service-a")
    private MsServiceAGrpc.MsServiceABlockingStub ms_ablockingStub;

    @GrpcClient("ms-service-b")
    private MsServiceBGrpc.MsServiceBBlockingStub msb_blockingStub;

    final Logger log = LoggerFactory.getLogger(GatewayService.class);

    public Flux<String> helloWorld1() {
        log.debug("GatewayService requesting to ServiceA");
        InputA input = InputA.newBuilder().build();

        return Flux.create(fluxSink -> {
            this.ms_ablockingStub.helloWorld(input).forEachRemaining(output -> fluxSink.next(output.getQuery()));
            fluxSink.complete();
            log.debug("GatewayService received response from ServiceA");
        });
    }

    public Flux<String> helloWorld2() {
        log.debug("GatewayService requesting to ServiceB");
        InputB input = InputB.newBuilder().build();

        return Flux.create(fluxSink -> {
            this.msb_blockingStub.helloWorld(input).forEachRemaining(output -> fluxSink.next(output.getQuery()));
            fluxSink.complete();
            log.debug("GatewayService received response from ServiceB");
        });
    }
}
