package com.ai.ServiceTest;

import org.apache.logging.log4j.message.ExitMessage;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.entity.Batch;
import com.ai.entity.BatchHasExam;
import com.ai.entity.Course;
import com.ai.entity.Exam;
import com.ai.entity.Module;
import com.ai.entity.Question;
import com.ai.entity.StudentHasExam;
import com.ai.repository.ExamRepository;
import com.ai.service.ExamService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ExamServiceTest {
    
    @Mock
    ExamRepository examRepository;

    @InjectMocks
    ExamService examService;

    public Exam examObj(){
        Exam exam = Exam.builder()
        .id(1)
        .title("Exam Title")
        .fullmark(100)
        .questions(new ArrayList<Question>())
        .course(courseOne())
        .batchHasExams(new ArrayList<BatchHasExam>())
        .studentHasExams(new ArrayList<StudentHasExam>())
        .courseId(1)
        .build();
        return exam;
    }

    public List<Exam> exams(){
        List<Exam> examList = new ArrayList<Exam>();

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

        Exam exam2 = Exam.builder()
        .id(2)
        .title("Exam Title 2")
        .fullmark(100)
        .questions(new ArrayList<Question>())
        .course(courseTwo())
        .batchHasExams(new ArrayList<BatchHasExam>())
        .studentHasExams(new ArrayList<StudentHasExam>())
        .courseId(2)
        .build();

        examList.add(exam1);
        examList.add(exam2);

        return examList;
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

    public Course courseTwo(){
        Course course2 = Course.builder()
        .id(2)
        .name("Course Two")
        .description("Description Two")
        .exams(new ArrayList<Exam>())
        .batches(new ArrayList<Batch>())
        .modules(new ArrayList<Module>())
        .build();
        return course2;
    }

    @Test
    public void saveTest(){
        Exam exam = examObj();
        examService.save(exam);
        verify(examRepository, timeout(1)).save(exam);
    }

    @Test
    public void findAllTest(){
        List<Exam> exams = exams();
        Mockito.when(examRepository.findAll()).thenReturn(exams);
        List<Exam> examList = examService.findAll();
        assertEquals(2, examList.size());
        verify(examRepository, times(1)).findAll();
    }

    @Test
    public void findByIdTest(){
        Exam exam = examObj();
        Mockito.when(examRepository.findById(1)).thenReturn(Optional.of(exam));
        Exam getExam = examService.findById(1);
        assertEquals("Exam Title", getExam.getTitle());
    }

    @Test
    public void deleteByIdTest(){
        examService.deleteById(1);
		verify(examRepository,times(1)).deleteById(1);
    }

    @Test
    public void findByCourseIdTest(){
        List<Exam> exams = exams();
        Mockito.when(examRepository.findByCourse_Id(1)).thenReturn(exams);
        List<Exam> examList = examService.findByCourseId(1);
        assertEquals(2, examList.size());
        verify(examRepository, times(1)).findByCourse_Id(1);
    }

}
