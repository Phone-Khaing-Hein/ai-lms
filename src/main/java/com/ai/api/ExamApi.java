package com.ai.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ai.entity.Answer;
import com.ai.entity.Exam;
import com.ai.entity.Question;
import com.ai.service.AnswerService;
import com.ai.service.CourseService;
import com.ai.service.ExamService;
import com.ai.service.QuestionService;

@RestController
public class ExamApi {

    @Autowired
    private ExamService examService;

    @Autowired
    private  QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private CourseService courseService;
    
    @PostMapping("admin/exam-create")
    public void createExam(@RequestBody Exam exam){
        
        var answerList = new ArrayList<Answer>();
        var questionList = new ArrayList<Question>();
        for(var a : exam.getAnswers()){
            var answer = new Answer();
            answer.setAnswer(a);
            answerList.add(answerService.save(answer));
        }
        var question = new Question();
        question.setAnswer(exam.getCorrectAnswer());
        question.setAnswers(answerList);
        question.setMark(exam.getMark());
        question.setQuestion(exam.getQuestion());
        questionList.add(questionService.save(question));

        exam.setQuestions(questionList);
        exam.setCourse(courseService.findById(exam.getCourseId()));
        examService.save(exam); 
    }
    
}
