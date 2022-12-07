package com.atoz.error;

import com.atoz.error.dto.ErrorResponseDTO;
import com.atoz.error.dto.MultipleErrorResponseDTO;
import com.atoz.error.exception.InvalidTokenException;
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
    public ResponseEntity<MultipleErrorResponseDTO> handleBeanValidationFailure(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity.badRequest()
                .body(new MultipleErrorResponseDTO(errorMessages));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handleUsernameNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO(ex.getMessage()));
    }

    // 패스워드가 불일치할 때 401 응답
    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handleIncorrectPassword(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handleInvalidToken(InvalidTokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handleDatabaseAccessFailure(DataAccessException ex) {
        String errorMessage = "";

        Throwable rootCause = ex.getRootCause();
        if (rootCause != null) {
            errorMessage = rootCause.getMessage();
        }

        return ResponseEntity.internalServerError()
                .body(new ErrorResponseDTO(errorMessage));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handleAuthenticationFailure(InternalAuthenticationServiceException ex) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponseDTO(ex.getMessage()));
    }
}