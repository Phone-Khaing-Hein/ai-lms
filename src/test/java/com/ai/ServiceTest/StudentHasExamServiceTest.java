package com.ai.ServiceTest;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.repository.StudentHasExamRepository;
import com.ai.service.StudentHasExamService;

@SpringBootTest
public class StudentHasExamServiceTest {
    
    @Mock
    StudentHasExamRepository studentHasExamRepository;

    @InjectMocks
    StudentHasExamService studentHasExamService;
    
}
