package com.ai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ai.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer>{

    List<Question> findByExamId(int examId);
    
}
