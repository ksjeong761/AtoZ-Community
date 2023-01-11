package com.atoz.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
public class LoadPostsRequestDto {
    @NotNull
    @Min(0)
    @Max(Long.MAX_VALUE)
    private long offset;

    @NotNull
    @Min(1)
    @Max(50)
    private long limit;
}
