package com.atoz.login;

import com.atoz.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "com.atoz")
public class ExceptionControllerAdvice {

    @ExceptionHandler(LoginValidationException.class)
    public ResponseEntity<ErrorResponse> handleLoginValidation(HttpServletRequest request, LoginValidationException e) {
        String remoteIp = getClientIP(request);

        log.error("remoteIp={}, exception={}, errorMessage={}", remoteIp, e.getClass().getSimpleName(), e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    private static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
