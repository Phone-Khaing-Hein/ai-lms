package com.ai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.entity.StudentHasExam;
import com.ai.repository.StudentHasExamRepository;

@Service
public class StudentHasExamService {

    @Autowired
    private StudentHasExamRepository studentHasExamRepository;

    public StudentHasExam getSheByStudentIdAndExamId(String studentId, Integer examId) {
        return studentHasExamRepository.findByStudent_LoginIdAndExam_Id(studentId, examId);
    }

    public void addStudentHasExam(StudentHasExam studentHasExam) {
        studentHasExamRepository.save(studentHasExam);
    }

}
