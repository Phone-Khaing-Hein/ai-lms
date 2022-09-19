package com.ai.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.entity.Exam;
import com.ai.repository.ExamRepository;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    public List<Exam> findAll() {
        return examRepository.findAll();
    }

    public Exam save(Exam exam) {
        return examRepository.save(exam);
    }

    public Exam findById(int examId) {
        return examRepository.findById(examId).get();
    }

    public void deleteById(int examId) {
        examRepository.deleteById(examId);
    }

    public List<Exam> findByCourseId(int id) {
        return examRepository.findByCourse_Id(id);
    }
    
}
