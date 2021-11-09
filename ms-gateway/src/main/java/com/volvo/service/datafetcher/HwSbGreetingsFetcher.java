package com.volvo.service.datafetcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.volvo.model.Greetings;
import com.volvo.service.InputB;
import com.volvo.service.MsServiceBGrpc;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Component
public class HwSbGreetingsFetcher implements DataFetcher<Greetings> {

    final Logger log = LoggerFactory.getLogger(HwSbGreetingsFetcher.class);

    @GrpcClient("ms-service-b")
    private MsServiceBGrpc.MsServiceBBlockingStub msb_blockingStub;

    @Override
    public Greetings get(DataFetchingEnvironment environment) {
        log.info("ms-service-b request processing ... ");
        log.debug("GraphQL redirected to ms-service-b request and it is processing ... ");
        InputB input = InputB.newBuilder().build();
        String greetingMessage = this.msb_blockingStub.helloWorld(input).next().getQuery();
        log.info("ms-service-b request processed successfully");
        log.debug("ms-service-b request processed successfully" + greetingMessage);
        return new Greetings(greetingMessage);
    }
}
