package com.ai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ai.entity.PrivateComment;

@Repository
public interface PrivateCommentRepository extends JpaRepository<PrivateComment, Integer>{

    List<PrivateComment> findByAssignment_Id(int assignmentId);
    
}
