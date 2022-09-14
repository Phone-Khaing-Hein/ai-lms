package com.ai.ServiceTest;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.repository.CommentRepository;
import com.ai.service.CommentService;

@SpringBootTest
public class CommentServiceTest {
    
    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommentService commentService;

    
}
