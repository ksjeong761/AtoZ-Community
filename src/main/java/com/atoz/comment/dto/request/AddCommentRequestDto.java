package com.atoz.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCommentRequestDto {

    @Min(1)
    private long postId;

    @Min(0)
    private long parentCommentId;

    @NotNull(message = "내용을 반드시 입력해주세요.")
    @Size(min = 1, max = 511, message = "1 ~ 511자 사이의 내용을 입력해주세요.")
    private String content;
}