package com.atoz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
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
public class ControllerExceptionAdvice {

    // 데이터 검증에 실패했을 때 400 응답
    @ExceptionHandler({
        MethodArgumentNotValidException.class
    })
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
        log.info("ControllerExceptionAdvice validationFailed");

        List<String> errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        Map<String, List<String>> responseBodyMap = new LinkedHashMap<>();
        responseBodyMap.put("errorMessages", errorMessages);

        return ResponseEntity.badRequest().body(responseBodyMap);
    }

    // 요청은 정상적이지만 데이터베이스 관련 예외가 발생했을 때 500 응답
    @ExceptionHandler({
        DataAccessException.class
    })
    public ResponseEntity<Object> handleDataAccess(DataAccessException ex) {
        log.info("ControllerExceptionAdvice databaseAccessFailed");

        String errorMessage = ex.getRootCause().getMessage();

        Map<String, String> responseBodyMap = new LinkedHashMap<>();
        responseBodyMap.put("errorMessage", errorMessage);

        return ResponseEntity.internalServerError().body(responseBodyMap);
    }
}
