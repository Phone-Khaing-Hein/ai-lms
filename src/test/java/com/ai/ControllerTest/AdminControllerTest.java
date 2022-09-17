package com.ai.ControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.ai.service.*;

// import antlr.collections.List;
import java.util.List;
import com.ai.entity.*;
import javax.annotation.Resource;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@ContextConfiguration
@WithUserDetails("ADM001")
public class AdminControllerTest {

    @Resource
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    CourseService courseService;


    @Test
    public void testHome() throws Exception {
        this.mockMvc.perform(get("/admin/home"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("courseCount"))
                .andExpect(view().name("ADM-DB001"));
    }

    @Test
    public void testCourseList() throws Exception {
        List<Course> courses=new ArrayList<>();
        Mockito.when(courseService.findAll()).thenReturn(courses);
        this.mockMvc.perform(get("/admin/course-list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ADM-CT001"));
    }
}
