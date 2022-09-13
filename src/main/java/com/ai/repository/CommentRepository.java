package com.ai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ai.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{

    List<Comment> findAllByOrderByIdDesc();

    List<Comment> findByVideo_Id(Integer videoId);
    
}
