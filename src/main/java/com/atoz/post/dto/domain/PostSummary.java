package com.atoz.post.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostSummary {
    private long postId;
    private String userId;

    private String title;
    private String hashtags;
    private String categories;

    private int likeCount;
    private int viewCount;

    private LocalDateTime createdAt;
}
