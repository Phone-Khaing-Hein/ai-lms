package com.ai.repository;

import com.ai.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Integer> {
    int countByModuleId(int moduleId);

    List<Video> findByModuleId(int moduleId);
}
