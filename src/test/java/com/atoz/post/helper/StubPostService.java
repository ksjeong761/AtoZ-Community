package com.atoz.post.helper;

import com.atoz.post.PostService;
import com.atoz.post.dto.AddPostDTO;
import com.atoz.post.dto.DeletePostDTO;
import com.atoz.post.dto.OpenPostDTO;
import com.atoz.post.dto.UpdatePostDTO;
import com.atoz.post.entity.PostEntity;

public class StubPostService implements PostService {
    @Override
    public void addPost(AddPostDTO addPostDTO) {

    }

    @Override
    public void updatePost(UpdatePostDTO updatePostDTO) {

    }

    @Override
    public void deletePost(DeletePostDTO deletePostDTO) {

    }

    @Override
    public PostEntity findById(OpenPostDTO openPostDTO) {
        return null;
    }
}
