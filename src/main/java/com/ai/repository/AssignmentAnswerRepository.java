package com.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ai.entity.AssignmentAnswer;

@Repository
public interface AssignmentAnswerRepository extends JpaRepository<AssignmentAnswer, Integer>{

    AssignmentAnswer findByAssignment_IdAndUser_LoginId(int id, String loginId);
    
}
