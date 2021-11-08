package com.volvo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MsGatewayExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MsGatewayExceptionHandler.class);

    /**
     * handleServerdown for handle the Service A or B down
     * 
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleServerdown(final Exception ex, final WebRequest request) {
        LOG.error("Error while executing api:" + request.getContextPath() + " Error message: " + ex.getMessage());
        return handleExceptionInternal(ex, "Bad Service Request.", new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
