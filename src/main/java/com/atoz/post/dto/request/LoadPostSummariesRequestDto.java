package com.atoz.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoadPostSummariesRequestDto {

    @Min(0)
    @Max(Long.MAX_VALUE)
    private long offset = 0;

    @Min(1)
    @Max(50)
    private long limit = 20;

    @Min(value = 0, message = "1 이상의 작성자 최소 나이를 입력해주세요.")
    @Max(value = 200, message = "200 이하의 작성자 최소 나이를 입력해주세요.")
    private int writerAgeMin = 0;

    @Min(value = 0, message = "1 이상의 작성자 최대 나이를 입력해주세요.")
    @Max(value = 200, message = "200 이하의 작성자 최대 나이를 입력해주세요.")
    private int writerAgeMax = 200;
}
