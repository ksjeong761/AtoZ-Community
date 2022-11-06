package com.atoz.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ErrorResponseDTO {
    private String errorMessage;
}
