package com.atoz.comment.dto.request;

import javax.validation.constraints.NotNull;

public class LoadCommentsRequestDto {

    @NotNull
    private long postId;
}