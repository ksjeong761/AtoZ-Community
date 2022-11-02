package com.atoz.error;

import lombok.Getter;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ErrorResponseDTO {
    private final List<String> errorMessages;

    public ErrorResponseDTO(MethodArgumentNotValidException ex) {
        errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());
    }

    public ErrorResponseDTO(DataAccessException ex) {
        errorMessages = new ArrayList<>();

        Throwable rootCause = ex.getRootCause();
        if (rootCause != null) {
            errorMessages.add(rootCause.getMessage());
        }
    }
}
