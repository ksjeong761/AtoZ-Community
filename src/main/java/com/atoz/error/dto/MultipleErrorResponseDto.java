package com.atoz.error.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MultipleErrorResponseDto {
    private final List<String> errorMessages;
}
