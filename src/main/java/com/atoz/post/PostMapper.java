package com.atoz.post;

import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.OpenPostRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import com.atoz.post.dto.response.OpenPostResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface PostMapper {

    void addPost(@Param("addPostRequestDto") AddPostRequestDto addPostRequestDto,
                 @Param("userId") String userId);

    void updatePost(UpdatePostRequestDto updatePostRequestDto);

    void deletePost(DeletePostRequestDto deletePostRequestDto);

    Optional<OpenPostResponseDto> findPostByPostId(long postId);
}
