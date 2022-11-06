package com.atoz.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MultipleErrorResponseDTO {
    private final List<String> errorMessages;
}
