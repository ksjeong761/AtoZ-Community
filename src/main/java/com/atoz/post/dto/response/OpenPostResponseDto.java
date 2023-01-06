package com.atoz.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenPostResponseDto {
    private long postId;
    private String userId;

    private String title;
    private String content;

    private int likeCount;
    private int viewCount;

    private String comments;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
