package com.ai.ServiceTest;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ai.repository.BatchHasExamRepository;
import com.ai.service.BatchHasExamService;

public class BatchHasExamServiceTest {
    
    @Mock
    BatchHasExamRepository batchHasExamRepository;

    @InjectMocks
    BatchHasExamService batchHasExamService;
    
}
