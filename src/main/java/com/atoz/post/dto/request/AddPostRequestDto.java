package com.atoz.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddPostRequestDto {
    @Nullable
    private long postId;

    private String title;
    private String content;

    @Size(max = 127)
    private String hashtags;

    @Size(max = 127)
    private String categories;
}
