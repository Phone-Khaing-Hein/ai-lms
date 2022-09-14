package com.ai.service;

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
    
}
