package com.ai.ServiceTest;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.entity.Answer;
import com.ai.repository.AnswerRepository;
import com.ai.service.AnswerService;

@SpringBootTest
public class AnswerServiceTest {
    
    @Mock
    AnswerRepository answerRepository;

    @InjectMocks
    AnswerService answerService;
}
