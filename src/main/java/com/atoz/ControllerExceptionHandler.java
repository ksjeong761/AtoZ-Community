package com.atoz;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    // Controller에서 검증에 실패했을 때 400 응답
    @ExceptionHandler({
        MethodArgumentNotValidException.class
    })
    public ResponseEntity<Object> validationFailed(MethodArgumentNotValidException ex) throws JsonProcessingException {
        log.info("ControllerExceptionHandler validationFailed");

        List<String> errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        Map<String, List<String>> responseBodyMap = new LinkedHashMap<>();
        responseBodyMap.put("errorMessages", errorMessages);

        return ResponseEntity.badRequest().body(responseBodyMap);
    }
}
