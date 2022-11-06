package com.atoz.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    // 데이터 검증에 실패했을 때 400 응답
    @ExceptionHandler
    public ResponseEntity<MultipleErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(new MultipleErrorResponseDTO(errorMessages));
    }

    // 요청은 정상적이지만 데이터베이스 관련 예외가 발생했을 때 500 응답
    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handleDataAccess(DataAccessException ex) {
        String errorMessage = "";

        Throwable rootCause = ex.getRootCause();
        if (rootCause != null) {
            errorMessage = rootCause.getMessage();
        }
        return ResponseEntity.internalServerError().body(new ErrorResponseDTO(errorMessage));
    }
}
