package com.ai.ServiceTest;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.repository.CourseRepository;
import com.ai.service.CourseService;
import com.ai.entity.Course;;

@SpringBootTest
public class CourseServiceTest {

    @Mock
    CourseRepository courseRepository;

    @InjectMocks
    CourseService courseService;

    // public Course courseObj(){
    //     Course course = Course.builder()
    //     .id(1)
    //     .name("OJT")
    //     .description("description")
    //     .modules(modules)
    //     .build();
    // }
    // @Test
    // public void saveCourseTest(){
    //     Course course = 
    // }
}
