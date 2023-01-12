package com.atoz.post;

import com.atoz.post.dto.domain.Post;
import com.atoz.post.dto.domain.PostSummary;
import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.LoadPostsRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import com.atoz.post.dto.response.OpenPostResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PostMapper {

    void addPost(@Param("addPostRequestDto") AddPostRequestDto addPostRequestDto,
                 @Param("userId") String userId);

    void updatePost(@Param("postId") long postId,
                    @Param("updatePostRequestDto") UpdatePostRequestDto updatePostRequestDto);

    void deletePost(@Param("postId") long postId,
                    @Param("deletePostRequestDto") DeletePostRequestDto deletePostRequestDto);

    Optional<Post> findPostByPostId(long postId);

    List<PostSummary> loadPosts(LoadPostsRequestDto loadPostsRequestDto);
}
