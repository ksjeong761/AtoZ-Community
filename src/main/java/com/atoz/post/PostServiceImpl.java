package com.atoz.post;

import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.dto.request.DeletePostRequestDto;
import com.atoz.post.dto.request.OpenPostRequestDto;
import com.atoz.post.dto.request.UpdatePostRequestDto;
import com.atoz.post.dto.response.OpenPostResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;

    @Override
    public void addPost(AddPostRequestDto addPostRequestDto) {
        postMapper.addPost(addPostRequestDto, loadUserIdFromContext());
    }

    @Override
    public void updatePost(UpdatePostRequestDto updatePostRequestDto) {
        checkOwner(updatePostRequestDto.getUserId());

        postMapper.updatePost(updatePostRequestDto);
    }

    @Override
    public void deletePost(DeletePostRequestDto deletePostRequestDto) {
        checkOwner(deletePostRequestDto.getUserId());

        postMapper.deletePost(deletePostRequestDto);
    }

    @Override
    public OpenPostResponseDto openPost(OpenPostRequestDto openPostRequestDto) {
        return postMapper.findPostByPostId(openPostRequestDto.getPostId())
                .orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다."));
    }

    private String loadUserIdFromContext() {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    private void checkOwner(String postOwner) {
        if (!postOwner.equals(loadUserIdFromContext())) {
            throw new AccessDeniedException("게시글을 작성한 사람만 접근할 수 있습니다.");
        }
    }
}
