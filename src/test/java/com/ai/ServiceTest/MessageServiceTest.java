package com.ai.ServiceTest;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.repository.MessageRepository;
import com.ai.service.MessageService;

@SpringBootTest
public class MessageServiceTest {
    
    @Mock
    MessageRepository messageRepository;

    @InjectMocks
    MessageService messageService;

    
}
