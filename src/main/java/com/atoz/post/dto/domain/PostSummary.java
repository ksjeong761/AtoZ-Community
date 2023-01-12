package com.atoz.post.dto.domain;

import java.time.LocalDateTime;

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
