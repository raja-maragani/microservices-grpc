package com.volvo.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.volvo.service.datafetcher.HwSaGreetingsFetcher;
import com.volvo.service.datafetcher.HwSbGreetingsFetcher;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Service
public class GatewayGraphQLService {

    final Logger log = LoggerFactory.getLogger(GatewayGraphQLService.class);

    @Autowired
    HwSaGreetingsFetcher hwSaGreetingsFetcher;

    @Autowired
    HwSbGreetingsFetcher hwSbGreetingsFetcher;

    private GraphQL graphQL;

    @Value("classpath:graphql/greetings.graphql")
    Resource resourceFile;

    // load schema at application start up
    @PostConstruct
    private void loadSchema() throws IOException {

        log.info("Loading the greetings.graphql schema");
        InputStream inputStream = resourceFile.getInputStream();
        String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        // parse schema
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(result);
        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
        log.info("GraphQL initilized successfully");
    }

    /**
     * RuntimeWiring for wiring the fetcher Ex helloWorldSA --> HwSaGreetingsFetcher
     * 
     * @return
     */
    private RuntimeWiring buildRuntimeWiring() {
        log.info("RuntimeWiring initilization ... ");
        return RuntimeWiring.newRuntimeWiring().type("Query", typeWiring -> typeWiring
                .dataFetcher("helloWorldSA", hwSaGreetingsFetcher)
                .dataFetcher("helloWorldSB", hwSbGreetingsFetcher))
                .build();
    }

    public GraphQL getGraphQL() {
        return graphQL;
    }
}
