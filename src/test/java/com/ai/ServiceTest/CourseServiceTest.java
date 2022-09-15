package com.ai.ServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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



    public Course courseObj(){
        Course course = Course.builder()
        .id(1)
        .name("OJT")
        .description("description")
        .build();
        return course;
    }

    public List<Course> courseList(){
        List<Course> courseList = new ArrayList<Course>();

        Course course1 = Course.builder()
        .id(1)
        .name("OJT")
        .description("description")
        .build();

        Course course2 = Course.builder()
        .id(2)
        .name("JWD")
        .description("description")
        .build();

        Course course3 = Course.builder()
        .id(3)
        .name("PFC")
        .description("description")
        .build();

        courseList.add(course1);
        courseList.add(course2);
        courseList.add(course3);

        return courseList;
    }

    @Test
    public void saveCourseTest(){
        Course course = courseObj();
        courseService.save(course);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    public void findAllCourseTest(){
        List<Course> courses = courseList();
        Mockito.when(courseRepository.findAll()).thenReturn(courses);
        List<Course> userList = courseService.findAll();
        assertEquals(3, userList.size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    public void findByIdTest(){
        Course course = courseObj();
        Mockito.when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        Course getCourse = courseService.findById(1);
        assertEquals("OJT", getCourse.getName());
        assertEquals("description", getCourse.getDescription());
    }

    @Test
    public void findByNameTest(){
        Course course = courseObj();
        Mockito.when(courseRepository.findByName("OJT")).thenReturn(course);
        Course getCourse = courseService.findByName("OJT");
        assertEquals(1, getCourse.getId());
        assertEquals("description", getCourse.getDescription());
    }

    // @Test
	// public void deleteByIdTest() {
	// 	courseService.deleteById("1");
	// 	verify(courseRepository,times(1)).deleteById("1");
	// }

    @Test
    public void getCountTest(){
        int serviceCount = courseService.getCount();
        int repositoryCount = (int) courseRepository.count();
        assertEquals(serviceCount, repositoryCount);
    }
}
