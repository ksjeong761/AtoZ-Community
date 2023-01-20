package com.atoz.comment.dto.domain;

import java.time.LocalDateTime;

public class Comment {
    private long commentId;

    private long postId;
    private long parentCommentId;
    private int depth;

    private String writerUserId;

    private String content;
    private int likeCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}