package com.atoz.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;

@Getter
@Builder
@AllArgsConstructor
public class LoadCommentsRequestDto {

    @Min(1)
    private long postId;
}