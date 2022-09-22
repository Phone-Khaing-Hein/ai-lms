package com.ai.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.entity.Assignment;
import com.ai.repository.AssignmentRepository;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    public void save(Assignment assignment) {
        assignmentRepository.save(assignment);
    }

    public List<Assignment> findAll() {
        return assignmentRepository.findAll();
    }

    public int count() {
        var assignment = assignmentRepository.findAll().stream().filter(a -> a.getStart().isBefore(LocalDateTime.now())).toList();
        return assignment.size();
    }

    public Assignment findById(int id) {
        return assignmentRepository.findById(id).get();
    }

    public void deleteById(int id) {
        assignmentRepository.deleteById(id);
    }
    
}
