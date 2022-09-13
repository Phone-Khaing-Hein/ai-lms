package com.ai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.entity.Comment;
import com.ai.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public List<Comment> findAllByOrderByIdDesc() {
        return commentRepository.findAllByOrderByIdDesc();
    }

    public void delete(int id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> findByVideoId(Integer videoId) {
        return commentRepository.findByVideo_Id(videoId);
    }
}
