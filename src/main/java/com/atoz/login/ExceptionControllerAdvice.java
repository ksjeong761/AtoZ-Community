package com.atoz.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "com.atoz")
public class ExceptionControllerAdvice {

    @ExceptionHandler(LoginValidationException.class)
    public ResponseEntity<Object> handleLoginValidation(LoginValidationException e) {
        log.info("ExceptionControllerAdvice.handleLoginValidation");

        String errorMessage = e.getMessage();
        Map<String, String> responseBodyMap = new HashMap<>();
        responseBodyMap.put("message", errorMessage);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBodyMap);
    }

}
