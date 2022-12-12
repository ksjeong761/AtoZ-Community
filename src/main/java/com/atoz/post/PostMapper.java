package com.atoz.post;

import com.atoz.post.dto.PostDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {

    void addPost(PostDto postDto);

    void updatePost(PostDto postDto);

    void deletePost(PostDto postDto);

    PostDto findById(PostDto postDto);
}
