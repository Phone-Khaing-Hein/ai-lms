package com.ai.ServiceTest;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.repository.ExamRepository;
import com.ai.service.ExamService;

@SpringBootTest
public class ExamServiceTest {
    
    @Mock
    ExamRepository examRepository;

    @InjectMocks
    ExamService examService;

}
