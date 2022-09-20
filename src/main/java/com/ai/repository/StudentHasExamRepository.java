package com.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ai.entity.StudentHasExam;

public interface StudentHasExamRepository  extends JpaRepository<StudentHasExam, Integer>{
    
    public StudentHasExam findByStudent_LoginIdAndExam_Id(String studentId, Integer examId);    

}
