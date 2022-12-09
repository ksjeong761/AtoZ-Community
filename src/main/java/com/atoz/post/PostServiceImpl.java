package com.atoz.post;

import com.atoz.post.entity.PostEntity;
import com.atoz.post.dto.AddPostDTO;
import com.atoz.post.dto.DeletePostDTO;
import com.atoz.post.dto.OpenPostDTO;
import com.atoz.post.dto.UpdatePostDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;

    @Override
    public void addPost(AddPostDTO addPostDTO) {
        String userId = loadUserIdFromContext();
        LocalDateTime now = LocalDateTime.now();

        PostEntity post = PostEntity.builder()
                .userId(userId)
                .title(addPostDTO.getTitle())
                .content(addPostDTO.getContent())
                .likeCount(0)
                .viewCount(0)
                .comments("")
                .createdAt(now)
                .updatedAt(now)
                .build();

        postMapper.addPost(post);
    }

    @Override
    public void updatePost(UpdatePostDTO updatePostDTO) {
        String userId = loadUserIdFromContext();
        LocalDateTime now = LocalDateTime.now();

        PostEntity post = PostEntity.builder()
                .postId(updatePostDTO.getPostId())
                .userId(userId)
                .title(updatePostDTO.getTitle())
                .content(updatePostDTO.getContent())
                .updatedAt(now)
                .build();

        postMapper.updatePost(post);
    }

    @Override
    public void deletePost(DeletePostDTO deletePostDTO) {
        String userId = loadUserIdFromContext();

        PostEntity post = PostEntity.builder()
                .postId(deletePostDTO.getPostId())
                .userId(userId)
                .build();

        postMapper.deletePost(post);
    }

    @Override
    public PostEntity findById(OpenPostDTO openPostDTO) {
        String userId = loadUserIdFromContext();

        PostEntity post = PostEntity.builder()
                .postId(openPostDTO.getPostId())
                .userId(userId)
                .build();

        return postMapper.findById(post);
    }

    private String loadUserIdFromContext() {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}
