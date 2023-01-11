package com.atoz.post.dto.response;

import com.atoz.post.dto.domain.PostSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoadPostsResponseDto {
    private List<PostSummary> postSummaries;
}