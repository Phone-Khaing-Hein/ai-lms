package com.ai.ServiceTest;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.repository.QuestionRepository;
import com.ai.service.QuestionService;

@SpringBootTest
public class QuestionServiceTest {
    
    @Mock
    QuestionRepository questionRepository;

    @InjectMocks
    QuestionService questionService;
    
}
