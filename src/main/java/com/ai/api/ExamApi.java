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
    private CourseService courseService;

    @PostMapping("admin/exam-create")
    public void addExam(@RequestBody Exam exam) {
        exam.setCourse(courseService.findById(exam.getCourseId()));
        for(var q: exam.getQuestions()){
            q.setExam(exam);
            for(var a: q.getAnswers()){
                a.setQuestion(q);
            }
        }
        examService.save(exam);
    }
    
    // @PostMapping("admin/question-create")
    // public Question createExam(@RequestBody Exam exam){
        
    //     var answerList = new ArrayList<Answer>();
    //     var questionList = new ArrayList<Question>();
    //     var question = new Question();
    //     question.setAnswer(exam.getCorrectAnswer());
    //     question.setMark(exam.getMark());
    //     question.setQuestion(exam.getQuestion());
    //     questionList.add(questionService.save(question));

    //     for(var a : exam.getAnswers()){
    //         var answer = new Answer();
    //         answer.setAnswer(a);
    //         answer.setQuestion(question);
    //         answerList.add(answerService.save(answer));
    //     }
    //     question.setAnswers(answerList);
    //     return question;
    // }
    
}
