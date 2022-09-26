package com.ai.ControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.ai.service.*;

import java.util.List;
import com.ai.entity.*;
import com.ai.entity.Module;
import com.ai.entity.User.Role;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@ContextConfiguration
@WithUserDetails("ADM001")
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "bean1")
    UserService userService;

    @MockBean(name = "bean2")
    CourseService courseService;

    public User userObj(){
        User admin = User.builder()
        .loginId("ADM002")
        .name("Admin User")
        .password("admin")
        .role(Role.Admin)
        .email("phyuthin2004@gmail.com")
        .photo("default.png")
        .isActive(true)
        .build();
        return admin;
    }

    public Course courseObj(){
        Course course = Course.builder()
        .id(1)
        .name("OJT")
        .description("description")
        .modules(new ArrayList<Module>())
        .batches(new ArrayList<Batch>())
        .exams(new ArrayList<Exam>())
        .build();
        return course;
    }

    public Module moduleObj(){
        Module module = Module.builder()
        .id(1)
        .name("Module Name")
        .course(new Course())
        .resources(new ArrayList<Resource>())
        .videos(new ArrayList<Video>())
        .schedule(new Schedule())
        .resourceCount(1)
        .videoCount(1)
        .build();
        return module;
    }
    
    @Test
    public void homeTest() throws Exception {
        User user = userObj();
        Mockito.when(userService.findByLoginId("ADM001")).thenReturn(user);
        this.mockMvc.perform(get("/admin/home"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("courseCount"))
                .andExpect(model().attributeExists("batchCount"))
                .andExpect(model().attributeExists("examCount"))
                .andExpect(model().attributeExists("studentCount"))
                .andExpect(model().attributeExists("batchName"))
                .andExpect(model().attributeExists("attendanceCount"))
                .andExpect(view().name("ADM-DB001"));
    }

    @Test
    public void courseListTest() throws Exception {
        User user = userObj();
        Mockito.when(userService.findByLoginId("ADM001")).thenReturn(user);
        this.mockMvc.perform(get("/admin/course-list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ADM-CT001"));
    }

    @Test
    public void courseCreateTest() throws Exception {
        User user = userObj();
        Mockito.when(userService.findByLoginId("ADM001")).thenReturn(user);
        this.mockMvc.perform(post("/admin/course-create"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-CT001"));
    }

    @Test
    public void courseCreateNotNullTest() throws Exception {
        User user = userObj();
        Mockito.when(userService.findByLoginId("ADM001")).thenReturn(user);
        Course course = courseObj();
        Mockito.when(courseService.findByName(course.getName())).thenReturn(course);
        this.mockMvc.perform(post("/admin/course-create").flashAttr("course", course))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/course-list"));
    }

    @Test
    public void courseDetailTest() throws Exception {
        this.mockMvc.perform(get("/admin/course-detail").param("courseId", "1"))
            .andExpect(model().attributeExists("modules"))
            .andExpect(model().attributeExists("courseName"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-MT001"));
    }

    @Test
    public void editCourseTest() throws NoSuchFileException {
        Course course = courseObj();
        try {
            this.mockMvc.perform(post("/admin/course-edit").param("id", "1").param("name", course.getName()).param("description", course.getDescription()))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/admin/course-list"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void deleteCourseTest() throws Exception {
        Course course = courseObj();
        this.mockMvc.perform(get("/admin/course-delete").param("courseId", "1").param("courseName", course.getName()))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/course-list"));
    }

    @Test
    public void moduleCreateTest() throws Exception {
        Module module = moduleObj();
        this.mockMvc.perform(post("/admin/module-create").param("courseId", "1").flashAttr("module", module))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-MT001"));
    }

    @Test
    public void moduleNotNullTest() throws Exception {
        
    }
}