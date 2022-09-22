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

import com.ai.entity.Exam;
import com.ai.entity.StudentHasExam;
import com.ai.entity.User;
import com.ai.repository.StudentHasExamRepository;
import com.ai.service.StudentHasExamService;

@SpringBootTest
public class StudentHasExamServiceTest {
    
    @Mock
    StudentHasExamRepository studentHasExamRepository;

    @InjectMocks
    StudentHasExamService studentHasExamService;
    
    public StudentHasExam she(){
        StudentHasExam she = StudentHasExam.builder()
        .id(1)
        .mark(100)
        .student(new User())
        .exam(new Exam())
        .build();
        return she;
    }

    public List<StudentHasExam> sheList(){
        List<StudentHasExam> sheList = new ArrayList<StudentHasExam>();

        StudentHasExam she1 = StudentHasExam.builder()
        .id(1)
        .mark(100)
        .student(new User())
        .exam(new Exam())
        .build();

        StudentHasExam she2 = StudentHasExam.builder()
        .id(2)
        .mark(100)
        .student(new User())
        .exam(new Exam())
        .build();

        sheList.add(she1);
        sheList.add(she2);

        return sheList;
    }

    @Test
    public void findAllTest(){
        List<StudentHasExam> sheList = sheList();
        Mockito.when(studentHasExamRepository.findAll()).thenReturn(sheList);
        List<StudentHasExam> shes = studentHasExamService.findAll();
        assertEquals(2, shes.size());
        verify(studentHasExamRepository, times(1)).findAll();
    }

    @Test
    public void getSheByStudentIdAndExamIdTest(){
        StudentHasExam she = she();
        Mockito.when(studentHasExamRepository.findByStudent_LoginIdAndExam_Id("STU001", 1)).thenReturn(she);
        StudentHasExam shes = studentHasExamService.getSheByStudentIdAndExamId("STU001", 1);
        assertEquals(1, shes.getId());
    }

    @Test
    public void addStudentHasExamTest(){
        StudentHasExam studentHasExam = she();
        studentHasExamService.addStudentHasExam(studentHasExam);
        verify(studentHasExamRepository, times(1)).save(studentHasExam);
    }

}
