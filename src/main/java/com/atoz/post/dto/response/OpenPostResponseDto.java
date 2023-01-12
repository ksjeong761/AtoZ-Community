package com.atoz.post.dto.response;

import com.atoz.post.dto.domain.Post;
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
    private Post post;
}
