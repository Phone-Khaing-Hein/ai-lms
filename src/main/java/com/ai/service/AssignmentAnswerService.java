package com.ai.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.entity.AssignmentAnswer;
import com.ai.repository.AssignmentAnswerRepository;

@Service
public class AssignmentAnswerService {

    @Autowired
    private AssignmentAnswerRepository assignmentAnswerRepository;

    public void save(AssignmentAnswer answer) {
        assignmentAnswerRepository.save(answer);
    }

    public List<AssignmentAnswer> findAll() {
        return assignmentAnswerRepository.findAll();
    }

    public AssignmentAnswer findById(int id) {
        return assignmentAnswerRepository.findById(id).get();
    }

    public AssignmentAnswer findByAssignment_IdAndUser_LoginId(int id, String loginId) {
        return assignmentAnswerRepository.findByAssignment_IdAndUser_LoginId(id, loginId);
    }
    
}
