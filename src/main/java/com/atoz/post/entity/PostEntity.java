package com.atoz.post.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostEntity {
    private String postId;
    private String userId;

    private String title;
    private String content;

    private int likeCount;
    private int viewCount;

    private String comments;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
