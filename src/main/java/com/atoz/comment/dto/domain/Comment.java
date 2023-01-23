package com.atoz.comment.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private long commentId;
    private long parentCommentId;
    private int depth;

    private long postId;
    private String userId;
    private String writerUserId;

    private String content;
    private int likeCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}