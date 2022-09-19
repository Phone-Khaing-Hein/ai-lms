package com.ai.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.entity.Batch;
import com.ai.entity.BatchHasExam;
import com.ai.repository.BatchHasExamRepository;

@Service
public class BatchHasExamService {

    @Autowired
    private BatchHasExamRepository batchHasExamRepository;

    public List<BatchHasExam> getBatchHasExamListByBatchId(int batchId) {
        return batchHasExamRepository.findByBatchId(batchId);
    }

    public BatchHasExam getBatchHasExamById(Integer id) {
        return batchHasExamRepository.findById(id).orElse(null);
    }

}
