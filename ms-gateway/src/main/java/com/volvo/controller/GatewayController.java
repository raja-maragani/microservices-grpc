package com.volvo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.volvo.service.GatewayGraphQLService;

@RestController
@RequestMapping("/api")
public class GatewayController {

    final Logger log = LoggerFactory.getLogger(GatewayController.class);

    @Autowired
    GatewayGraphQLService gatewayGraphQLService;

    @PostMapping
    public ResponseEntity<Object> getHwGreetings1(@RequestBody String query) {
        log.debug("Request with " + query);
        String message = gatewayGraphQLService.getGraphQL().execute(query).getData().toString();
        if (message.contains("null")) {
            log.debug("Request processed and requested Service is Down");
            return new ResponseEntity<>("Service Down. Please try after some time", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.debug("Request processed and result is: " + message);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
