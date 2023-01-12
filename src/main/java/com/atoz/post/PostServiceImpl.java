package com.atoz.post;

import com.atoz.post.dto.domain.Post;
import com.atoz.post.dto.domain.PostSummary;
import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.LoadPostsRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import com.atoz.post.dto.response.LoadPostsResponseDto;
import com.atoz.post.dto.response.OpenPostResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;

    @Override
    public void addPost(AddPostRequestDto addPostRequestDto, UserDetails userDetails) {
        postMapper.addPost(addPostRequestDto, userDetails.getUsername());
    }

    @Override
    public void updatePost(long postId, UpdatePostRequestDto updatePostRequestDto) {
        postMapper.updatePost(postId, updatePostRequestDto);
    }

    @Override
    public void increaseLikeCount(long postId) {
        postMapper.increaseLikeCount(postId);
    }

    @Override
    public void deletePost(long postId, DeletePostRequestDto deletePostRequestDto) {
        postMapper.deletePost(postId, deletePostRequestDto);
    }

    @Override
    public OpenPostResponseDto openPost(long postId) {
        postMapper.increaseViewCount(postId);

        Post post = postMapper.findPostByPostId(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다."));
        return OpenPostResponseDto.builder()
                .post(post)
                .build();
    }

    @Override
    public LoadPostsResponseDto loadPosts(LoadPostsRequestDto loadPostsRequestDto) {
        List<PostSummary> postSummaries = postMapper.loadPosts(loadPostsRequestDto);
        return LoadPostsResponseDto.builder()
                .postSummaries(postSummaries)
                .build();
    }
}
