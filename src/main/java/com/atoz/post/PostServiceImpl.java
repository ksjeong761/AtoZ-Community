package com.atoz.post;

import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.OpenPostRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import com.atoz.post.dto.response.OpenPostResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;

    @Override
    public void addPost(AddPostRequestDto addPostRequestDto, UserDetails userDetails) {
        postMapper.addPost(addPostRequestDto, userDetails.getUsername());
    }

    @Override
    public void updatePost(UpdatePostRequestDto updatePostRequestDto) {
        postMapper.updatePost(updatePostRequestDto);
    }

    @Override
    public void deletePost(DeletePostRequestDto deletePostRequestDto) {
        postMapper.deletePost(deletePostRequestDto);
    }

    @Override
    public OpenPostResponseDto openPost(OpenPostRequestDto openPostRequestDto) {
        return postMapper.findPostByPostId(openPostRequestDto.getPostId())
                .orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다."));
    }
}
