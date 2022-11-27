package com.atoz.error;

import com.atoz.error.dto.ErrorResponseDTO;
import com.atoz.error.dto.MultipleErrorResponseDTO;
import com.atoz.error.exception.InvalidTokenException;
import com.atoz.error.exception.UnAuthorizedException;
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

    // 유저 아이디가 존재하지 않을 때 401 응답
    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handleUsernameNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDTO(ex.getMessage()));
    }

    // 패스워드가 불일치할 때 401 응답
    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handlePasswordIncorrect(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDTO(ex.getMessage()));
    }

    // 유효하지 않은 토큰일 때 401 응답
    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handleInvalidToken(InvalidTokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDTO(ex.getMessage()));
    }

    // 요청에 대한 권한이 없 때 403 응답
    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handleInvalidToken(UnAuthorizedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponseDTO(ex.getMessage()));
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

    // 내부 인증 로직 중 알 수 없는 예외 발생 시 500 응답
    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handleAuthFailed(InternalAuthenticationServiceException ex) {
        return ResponseEntity.internalServerError().body(new ErrorResponseDTO(ex.getMessage()));
    }
}