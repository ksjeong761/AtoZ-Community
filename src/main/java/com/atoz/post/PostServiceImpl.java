package com.atoz.post;

import com.atoz.error.exception.NoRowsFoundException;
import com.atoz.post.dto.domain.Post;
import com.atoz.post.dto.domain.PostSummary;
import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.LoadPostSummariesRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import com.atoz.post.dto.response.LoadPostSummariesResponseDto;
import com.atoz.post.dto.response.OpenPostResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;

    @Override
    public void addPost(AddPostRequestDto addPostRequestDto, String userId) {
        postMapper.addPost(addPostRequestDto, userId);
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
        long affectedRows = postMapper.increaseViewCount(postId);
        if (affectedRows == 0) {
            throw new NoRowsFoundException("게시글이 존재하지 않습니다.");
        }

        Optional<Post> post = postMapper.findPostByPostId(postId);
        if (post.isEmpty()) {
            throw new NoRowsFoundException("게시글이 존재하지 않습니다.");
        }
        return OpenPostResponseDto.builder()
                .post(post.get())
                .build();
    }

    @Override
    public LoadPostSummariesResponseDto loadPostSummaries(LoadPostSummariesRequestDto loadPostSummariesRequestDto) {
        List<PostSummary> postSummaries = postMapper.loadPostSummaries(loadPostSummariesRequestDto);
        return LoadPostSummariesResponseDto.builder()
                .postSummaries(postSummaries)
                .build();
    }
}
