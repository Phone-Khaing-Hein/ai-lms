package com.ai.ServiceTest;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.repository.AssignmentAnswerRepository;
import com.ai.service.AssignmentAnswerService;

@SpringBootTest
public class AssignmentAnswerServiceTest {
    
    @Mock
    AssignmentAnswerRepository assignmentAnswerRepository;

    @InjectMocks
    AssignmentAnswerService assignmentAnswerService;

    
}
