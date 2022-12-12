package com.atoz.post.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private int postId;
    private String userId;

    private String title;
    private String content;

    private int likeCount;
    private int viewCount;

    private String comments;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
