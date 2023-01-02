package com.atoz.post;

import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.OpenPostRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import com.atoz.post.dto.response.OpenPostResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface PostService {

    void addPost(AddPostRequestDto addPostRequestDto, UserDetails userDetails);

    void updatePost(UpdatePostRequestDto updatePostRequestDto);

    void deletePost(DeletePostRequestDto deletePostRequestDto);

    OpenPostResponseDto openPost(OpenPostRequestDto openPostRequestDto);
}
