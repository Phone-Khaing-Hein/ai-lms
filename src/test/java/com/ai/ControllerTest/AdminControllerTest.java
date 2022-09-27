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

    // public Module moduleObj(){
    //     Module module = Module.builder()
    //     .id(1)
    //     .name("Module Name")
    //     .course(new Course())
    //     .resources(new ArrayList<Resource>())
    //     .videos(new ArrayList<Video>())
    //     .schedule(new Schedule())
    //     .resourceCount(1)
    //     .videoCount(1)
    //     .build();
    //     return module;
    // }
    
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
        this.mockMvc.perform(get("/admin/course-detail").param("courseId", "3"))
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

    // @Test
    // public void deleteCourseTest() throws Exception {
    //     Course course = courseObj();
    //     this.mockMvc.perform(get("/admin/course-delete").param("courseId", "2").param("courseName", course.getName()))
    //         .andExpect(status().is(302))
    //         .andExpect(redirectedUrl("/admin/course-list"));
    // }

    // @Test
    // public void moduleCreateTest() throws Exception {
    //     Module module = moduleObj();
    //     this.mockMvc.perform(post("/admin/module-create").param("courseId", "1").flashAttr("module", module))
    //         .andExpect(status().isOk())
    //         .andExpect(view().name("ADM-MT001"));
    // }

    @Test
    public void moduleNotNullTest() throws Exception {

    }

    @Test
    public void resourceListTest() throws Exception {
        this.mockMvc.perform(get("/admin/resource-list").param("moduleId", "3"))
            .andExpect(model().attributeExists("resources"))
            .andExpect(model().attributeExists("moduleName"))
            .andExpect(model().attributeExists("course"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-RS001"));
    }

    @Test
    public void moduleEditTest() throws Exception {
        
    }

    @Test
    public void moduleDeleteTest() throws Exception {

    }

    @Test
    public void resourceCreateTest() throws Exception {

    }

    @Test
    public void resourceDeleteTest() throws Exception {

    }

    @Test
    public void videoListTest() throws Exception {

    }

    @Test
    public void createVideoTest() throws Exception {

    }

    @Test
    public void deleteVideoTest() throws Exception {

    }

    @Test
    public void batchListTest() throws Exception {

    }

    @Test
    public void batchCreateBindingResultTest() throws Exception {

    }

    @Test
    public void batchNotNullTest() throws Exception {

    }

    @Test
    public void batchDateTest() throws Exception {

    }

    @Test
    public void batchEditTest() throws Exception {

    }

    @Test
    public void batchDeleteTest() throws Exception {

    }

    @Test
    public void closeBatchTest() throws Exception {

    }

    @Test
    public void openBatchTest() throws Exception {

    }

    @Test
    public void createStudentBindingResultTest() throws Exception {

    }

    @Test
    public void createStudentBatchNullTest() throws Exception {

    }

    @Test
    public void createStudentIdExistsTest() throws Exception {

    }

    @Test
    public void createStudentEmailExistsTest() throws Exception {

    }

    @Test
    public void createStudentTest() throws Exception {

    }

    @Test
    public void studentListTest() throws Exception {

    }

    @Test
    public void studentEditTest() throws Exception {

    }

    @Test
    public void studentDeleteTest() throws Exception {

    }

    @Test
    public void usersTest() throws Exception {

    }

    @Test
    public void studentTest() throws Exception {

    }

    @Test
    public void batchesTest() throws Exception {

    }

    @Test
    public void coursesTest() throws Exception {

    }

    @Test
    public void courseTest() throws Exception {

    }

    @Test
    public void moduleTest() throws Exception {

    }

    @Test
    public void batchTest() throws Exception {

    }

    @Test
    public void userTest() throws Exception {

    }

    @Test
    public void loginUserTest() throws Exception {

    }

    @Test
    public void createTeacherBindingResultTest() throws Exception {

    }

    @Test
    public void createTeacherIdExistsTest() throws Exception {

    }

    @Test
    public void createTeacherTest() throws Exception {

    }

    @Test
    public void teacherListTest() throws Exception {

    }

    @Test
    public void teacherEditTest() throws Exception {

    }

    @Test
    public void deleteTeacherTest() throws Exception {

    }

    @Test
    public void attendanceListTest() throws Exception {

    }

    @Test
    public void examListTest() throws Exception {

    }

    @Test
    public void examCreateTest() throws Exception {

    }

    @Test
    public void examDeleteTest() throws Exception {

    }

    @Test
    public void examDetailTest() throws Exception {

    }

    @Test
    public void profileTest() throws Exception {

    }

    @Test
    public void changePasswordTest() throws Exception {

    }

    @Test
    public void passwordLengthTest() throws Exception {

    }

    @Test
    public void oldPasswordTest() throws Exception{

    }

    @Test
    public void newPasswordTest() throws Exception {

    }

    @Test
    public void resetPasswordTest() throws Exception {
        
    }

} 