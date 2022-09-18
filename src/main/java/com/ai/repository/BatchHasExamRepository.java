package com.ai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ai.entity.BatchHasExam;

@Repository
public interface BatchHasExamRepository extends JpaRepository<BatchHasExam, Integer>{

    List<BatchHasExam> findByBatchId(int batchId);
    
}
