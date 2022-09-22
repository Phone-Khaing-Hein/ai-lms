package com.ai.ServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.entity.Answer;
import com.ai.entity.Course;
import com.ai.entity.Exam;
import com.ai.entity.Question;
import com.ai.entity.StudentHasExam;
import com.ai.entity.Batch;
import com.ai.entity.BatchHasExam;
import com.ai.entity.Module;
import com.ai.repository.QuestionRepository;
import com.ai.service.QuestionService;

@SpringBootTest
public class QuestionServiceTest {
    
    @Mock
    QuestionRepository questionRepository;

    @InjectMocks
    QuestionService questionService;

    public Question question(){
        Question question = Question.builder()
        .id(1)
        .question("Question")
        .correctAnswer("correctAnswer")
        .mark(100)
        .exam(new Exam())
        .answers(new ArrayList<Answer>())
        .build();
        return question;
    }

    public List<Question> questions(){
        List<Question> questions = new ArrayList<Question>();

        Question question1 = Question.builder()
        .id(1)
        .question("Question 1")
        .correctAnswer("correctAnswer 1")
        .mark(100)
        .exam(examOne())
        .answers(new ArrayList<Answer>())
        .build();

        Question question2 = Question.builder()
        .id(2)
        .question("Question 2")
        .correctAnswer("correctAnswer 2")
        .mark(100)
        .exam(examTwo())
        .answers(new ArrayList<Answer>())
        .build();

        questions.add(question1);
        questions.add(question2);

        return questions;
    }

    public Exam examOne(){
        Exam exam1 = Exam.builder()
        .id(1)
        .title("Exam Title 1")
        .fullmark(100)
        .questions(new ArrayList<Question>())
        .course(courseOne())
        .batchHasExams(new ArrayList<BatchHasExam>())
        .studentHasExams(new ArrayList<StudentHasExam>())
        .courseId(1)
        .build();
        return exam1;
    }

    public Exam examTwo(){
        Exam exam2 = Exam.builder()
        .id(2)
        .title("Exam Title 2")
        .fullmark(100)
        .questions(new ArrayList<Question>())
        .course(courseOne())
        .batchHasExams(new ArrayList<BatchHasExam>())
        .studentHasExams(new ArrayList<StudentHasExam>())
        .courseId(1)
        .build();
        return exam2;
    }
    
    public Course courseOne(){
        Course course1 = Course.builder()
        .id(1)
        .name("Course One")
        .description("Description One")
        .exams(new ArrayList<Exam>())
        .batches(new ArrayList<Batch>())
        .modules(new ArrayList<Module>())
        .build();
        return course1;
    }

    @Test
    public void saveTest(){
        Question question = question();
        questionService.save(question);
        verify(questionRepository, times(1)).save(question);
    }

    @Test
    public void findByExamIdTest(){
        List<Question> questions = questions();
        Mockito.when(questionRepository.findByExamId(1)).thenReturn(questions);
        List<Question> questionList = questionService.findByExamId(1);
        assertEquals(2, questionList.size());
        verify(questionRepository, times(1)).findByExamId(1);
    }
    
}
