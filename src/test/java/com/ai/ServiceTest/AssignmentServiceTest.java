package com.ai.ServiceTest;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.repository.AssignmentRepository;
import com.ai.service.AssignmentService;

@SpringBootTest
public class AssignmentServiceTest {
    
    @Mock
    AssignmentRepository assignmentRepository;

    @InjectMocks
    AssignmentService assignmentService;

    
}
