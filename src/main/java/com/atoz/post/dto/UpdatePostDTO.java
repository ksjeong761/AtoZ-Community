package com.atoz.post.dto;

import lombok.Getter;

@Getter
public class UpdatePostDTO {
    private String postId;
    private String title;
    private String content;
}
