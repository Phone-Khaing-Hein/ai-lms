package com.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ai.entity.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer>{
    
}
