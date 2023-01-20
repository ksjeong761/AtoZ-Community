package com.atoz.comment.dto.request;

import javax.validation.constraints.NotNull;

public class UpdateCommentRequestDto {

    @NotNull
    private long commentId;

    @NotNull
    private long postId;

    @NotNull
    private String writerUserId;

    @NotNull
    private String content;
}