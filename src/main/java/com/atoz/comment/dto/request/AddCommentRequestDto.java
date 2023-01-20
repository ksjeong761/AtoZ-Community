package com.atoz.comment.dto.request;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

public class AddCommentRequestDto {

    @Nullable
    private long postId;

    @NotNull
    private long parentCommentId;

    @NotNull
    private String writerUserId;

    @NotNull
    private String content;
}