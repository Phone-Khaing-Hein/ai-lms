package com.ai.ServiceTest;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.repository.ModuleRepository;
import com.ai.service.ModuleService;

@SpringBootTest
public class ModuleServiceTest {
    
    @Mock
    ModuleRepository moduleRepository;

    @InjectMocks
    ModuleService moduleService;

    
}
