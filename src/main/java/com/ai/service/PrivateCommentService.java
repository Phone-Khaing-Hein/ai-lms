package com.ai.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.entity.PrivateComment;
import com.ai.repository.PrivateCommentRepository;

@Service
public class PrivateCommentService {

    @Autowired
    private PrivateCommentRepository privateCommentRepository;

    public PrivateComment save(PrivateComment privateComment) {
        return privateCommentRepository.save(privateComment);
    }

    public List<PrivateComment> findByAssignmentId(int assignmentId) {
        return privateCommentRepository.findByAssignment_Id(assignmentId);
    }
    
}
