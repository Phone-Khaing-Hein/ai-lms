package com.ai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ai.entity.Exam;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer>{

    List<Exam> findByCourse_Id(int id);
    
}
