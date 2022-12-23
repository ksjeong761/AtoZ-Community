package com.atoz.error;

import com.atoz.error.dto.ErrorResponseDto;
import com.atoz.error.dto.MultipleErrorResponseDto;
import com.atoz.error.exception.JwtAuthenticationException;
import com.atoz.error.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler
    public ResponseEntity<MultipleErrorResponseDto> handleBeanValidationFailure(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity.badRequest()
                .body(new MultipleErrorResponseDto(errorMessages));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleUsernameNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto(ex.getMessage()));
    }

    // 패스워드가 불일치할 때 401 응답
    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleIncorrectPassword(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto(ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleInvalidToken(JwtAuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto(ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDto(ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleDatabaseAccessFailure(DataAccessException ex) {
        String errorMessage = "";

        Throwable rootCause = ex.getRootCause();
        if (rootCause != null) {
            errorMessage = rootCause.getMessage();
        }

        return ResponseEntity.internalServerError()
                .body(new ErrorResponseDto(errorMessage));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleAuthenticationFailure(InternalAuthenticationServiceException ex) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponseDto(ex.getMessage()));
    }
}