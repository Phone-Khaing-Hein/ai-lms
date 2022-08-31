package com.ai.repository;

import com.ai.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer> {
    int countByModuleId(int moduleId);

    List<Resource> findByModuleId(int moduleId);
}
