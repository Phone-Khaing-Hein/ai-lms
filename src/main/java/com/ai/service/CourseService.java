package com.ai.service;

import com.ai.entity.Course;
import com.ai.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private FileService fileService;

    public void save(Course course) {
        courseRepo.save(course);
    }

    public List<Course> findAll(){
        return courseRepo.findAll();
    }

    public Course findById(int courseId) {
        return courseRepo.findById(courseId).get();
    }

    public Course findByName(String name) {
        return courseRepo.findByName(name);
    }

    public void deleteById(int courseId, String courseName) throws IOException {
        fileService.deleteFolder(courseName);
        courseRepo.deleteById(courseId);
    }

    public int getCount() {
        return (int) courseRepo.count();
    }
}
