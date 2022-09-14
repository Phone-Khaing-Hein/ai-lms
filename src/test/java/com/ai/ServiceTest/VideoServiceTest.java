package com.ai.ServiceTest;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.repository.VideoRepository;
import com.ai.service.VideoService;

@SpringBootTest
public class VideoServiceTest {
    
    @Mock
    VideoRepository videoRepository;

    @InjectMocks
    VideoService videoService;

    
}
