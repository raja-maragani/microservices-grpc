package com.volvo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URL;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GatewayControllerTest {

    // bind the above RANDOM_PORT
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testSAdown() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("{ helloWorldSA { greetings } }", headers);
        ResponseEntity<String> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/api").toString(), request, String.class);
        assertEquals("Service Down. Please try after some time", response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSBdown() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("{ helloWorldSB { greetings } }", headers);
        ResponseEntity<String> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/api").toString(), request, String.class);
        assertEquals("Service Down. Please try after some time", response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSCBadRequest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>("{ helloWorldSC { greetings } }", headers);
        ResponseEntity<String> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/api").toString(), request, String.class);
        assertEquals("Bad Service Request.", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
