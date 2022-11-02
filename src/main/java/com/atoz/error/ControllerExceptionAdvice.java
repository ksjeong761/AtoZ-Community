package com.atoz.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionAdvice {

    // 데이터 검증에 실패했을 때 400 응답
    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(ex));
    }

    // 요청은 정상적이지만 데이터베이스 관련 예외가 발생했을 때 500 응답
    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handleDataAccess(DataAccessException ex) {
        return ResponseEntity.internalServerError().body(new ErrorResponseDTO(ex));
    }
}
