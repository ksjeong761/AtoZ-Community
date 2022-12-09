package com.atoz.post;

import com.atoz.post.entity.PostEntity;
import com.atoz.post.dto.DeletePostDTO;
import com.atoz.post.dto.OpenPostDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {

    void addPost(PostEntity postEntity);

    void updatePost(PostEntity postEntity);

    void deletePost(PostEntity postEntity);

    PostEntity findById(PostEntity postEntity);
}
