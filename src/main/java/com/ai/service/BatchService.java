package com.ai.service;

import com.ai.entity.Batch;
import com.ai.repository.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchService {

    @Autowired
    private BatchRepository batchRepository;

    public Batch findByName(String name) {
        return batchRepository.findByName(name);
    }

    public void save(Batch batch) {
        batchRepository.save(batch);
    }

    public List<Batch> findAll() {
        return batchRepository.findAll();
    }

    public Batch findById(Integer id) {
        return batchRepository.findById(id).get();
    }
}
