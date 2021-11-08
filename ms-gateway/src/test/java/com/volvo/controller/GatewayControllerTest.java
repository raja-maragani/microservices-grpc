package com.volvo.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(GatewayController.class)
public class GatewayControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Test
    public void testSendMailSuccess() throws Exception {

        /*This test scenario for test the /send-email/post rest endpoint with the positive parameters  */
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api")
                .accept(MediaType.ALL)
                .content("{ helloWorldSA { greetings } }");
        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final String expected = "Validation error of type FieldUndefined";
        final MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains(expected));
    }
}
