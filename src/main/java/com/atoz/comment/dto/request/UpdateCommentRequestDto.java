package com.atoz.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentRequestDto {

    @NotNull
    private long commentId;

    @NotNull
    @Min(1)
    @Max(Long.MAX_VALUE)
    private long postId;

    @NotNull(message = "아이디를 반드시 입력해주세요.")
    @Size(min = 1, max = 20, message = "1 ~ 20자 사이의 아이디를 입력해주세요.")
    private String userId;

    @NotNull(message = "내용을 반드시 입력해주세요.")
    @Size(min = 1, max = 511, message = "1 ~ 511자 사이의 내용을 입력해주세요.")
    private String content;
}