package com.ai.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.entity.BatchHasExam;
import com.ai.entity.Exam;
import com.ai.entity.StudentHasExam;
import com.ai.entity.User;
import com.ai.service.BatchHasExamService;
import com.ai.service.ExamService;
import com.ai.service.StudentHasExamService;
import com.ai.service.UserService;

@RestController
@RequestMapping("/api/studentExam")
public class StudentExamApi {

    @Autowired
    private BatchHasExamService batchHasExamService;

    @Autowired
    private StudentHasExamService studentHasExamService;

    @Autowired
    private ExamService examService;

    @Autowired
    private UserService userService;

    @GetMapping("/getBheById/{bheId}")
    public BatchHasExam getBheById(@PathVariable("bheId") Integer bheId) {
        return batchHasExamService.getBatchHasExamById(bheId);
    }

    @GetMapping("/getStudentHasExam/{studentId}/{examId}")
    public StudentHasExam getStudentHasExam(@PathVariable("studentId") String studentId,
            @PathVariable("examId") Integer examId) {
        return studentHasExamService.getSheByStudentIdAndExamId(studentId, examId);
    }

    @PostMapping("/addStudentHasExam")
    public void addStudentHasExam(@Param("studentId") String studentId, @Param("examId") Integer examId,
            @Param("score") Integer score) {

        StudentHasExam studentHasExamRes = studentHasExamService.getSheByStudentIdAndExamId(studentId,
                examId);
        if (studentHasExamRes == null) {
            User student = userService.findByLoginId(studentId);
            Exam exam = examService.findById(examId);
            StudentHasExam studentHasExam = new StudentHasExam();
            studentHasExam.setExam(exam);
            studentHasExam.setStudent(student);
            studentHasExam.setMark(score);
            studentHasExamService.addStudentHasExam(studentHasExam);
        }

    }

}
