package com.atoz.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    @Override
    public void loadComments() {
        commentMapper.loadComments();
    }

    @Override
    public void addComment() {
        commentMapper.addComment();
    }

    @Override
    public void updateComment() {
        commentMapper.updateComment();
    }

    @Override
    public void deleteComment() {
        commentMapper.deleteComment();
    }
}