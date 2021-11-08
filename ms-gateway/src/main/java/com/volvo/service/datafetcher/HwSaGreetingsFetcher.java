package com.volvo.service.datafetcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.volvo.model.Greetings;
import com.volvo.service.InputA;
import com.volvo.service.MsServiceAGrpc;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Component
public class HwSaGreetingsFetcher implements DataFetcher<Greetings> {

    final Logger log = LoggerFactory.getLogger(HwSaGreetingsFetcher.class);

    @GrpcClient("ms-service-a")
    private MsServiceAGrpc.MsServiceABlockingStub ms_ablockingStub;

    @Override
    public Greetings get(DataFetchingEnvironment environment) throws StatusRuntimeException {
        log.info("Service A request processing ... ");
        log.debug("GraphQL redirected to Service A request and it is processing ... ");
        InputA input = InputA.newBuilder().build();
        String greetingMessage;
        greetingMessage = this.ms_ablockingStub.helloWorld(input).next().getQuery();
        log.info("Service B request processed successfully");
        log.debug("Service B request processed successfully" + greetingMessage);
        return new Greetings(greetingMessage);
    }
}
