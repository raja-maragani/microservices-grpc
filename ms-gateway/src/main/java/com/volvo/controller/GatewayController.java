package com.volvo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.volvo.service.GatewayService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class GatewayController {
    
    final Logger log = LoggerFactory.getLogger(GatewayController.class);

    @Autowired
    GatewayService gatewayService;
    
    @GetMapping("/hw-SA")
    public Flux<String> getHwGreetings1(){
        log.info("Gateway request: hw-greetings1");
        return this.gatewayService.helloWorld1();
    }
    
    @GetMapping("/hw-SB")
    public Flux<String> getHwGreetings2(){
        log.info("Gateway request: hw-greetings2");
        return this.gatewayService.helloWorld2();
    }
    
    @GetMapping("/hw-SC")
    public String getHwGreetings3(){
        log.info("Gateway request: hw-greetings2");
        return "Service C Hello-World";
    }
    
}
