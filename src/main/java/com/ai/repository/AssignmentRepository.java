package com.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ai.entity.Assignment;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer>{
    
}
