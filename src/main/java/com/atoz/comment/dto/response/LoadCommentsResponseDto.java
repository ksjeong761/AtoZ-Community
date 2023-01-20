package com.atoz.comment.dto.response;

import com.atoz.comment.dto.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoadCommentsResponseDto {
    private List<Comment> comments;
}