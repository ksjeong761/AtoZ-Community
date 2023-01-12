package com.atoz.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostRequestDto {
    private String userId;

    private String title;
    private String content;

    @Size(max = 127)
    private String hashtags;

    @Size(max = 127)
    private String categories;
}
