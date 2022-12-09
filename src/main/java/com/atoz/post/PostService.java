package com.atoz.post;

import com.atoz.post.entity.PostEntity;
import com.atoz.post.dto.AddPostDTO;
import com.atoz.post.dto.DeletePostDTO;
import com.atoz.post.dto.OpenPostDTO;
import com.atoz.post.dto.UpdatePostDTO;

public interface PostService {

    void addPost(AddPostDTO addPostDTO);

    void updatePost(UpdatePostDTO updatePostDTO);

    void deletePost(DeletePostDTO deletePostDTO);

    PostEntity findById(OpenPostDTO openPostDTO);
}
