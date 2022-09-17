package com.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ai.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer>{
    
}
