package com.atoz.comment.dto.request;

import javax.validation.constraints.NotNull;

public class DeleteCommentRequestDto {

    @NotNull
    private long commentId;
}