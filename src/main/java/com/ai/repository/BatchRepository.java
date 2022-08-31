package com.ai.repository;

import com.ai.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchRepository extends JpaRepository<Batch, Integer> {
    Batch findByName(String name);
}
