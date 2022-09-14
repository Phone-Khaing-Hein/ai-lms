package com.ai.ServiceTest;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.repository.ResourceRepository;
import com.ai.service.ResourceService;

@SpringBootTest
public class ResourceServiceTest {
    
    @Mock
    ResourceRepository resourceRepository;

    @InjectMocks
    ResourceService resourceService;

    
}
