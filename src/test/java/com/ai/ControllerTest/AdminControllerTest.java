package com.ai.ControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.ai.service.*;

// import antlr.collections.List;
import java.util.List;
import com.ai.entity.*;
import com.ai.entity.User.Role;

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

    @Mock
	MockHttpSession sessionMock;

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
    
    @Test
    public void homeTest() throws Exception {
        User admin = userObj();
        sessionMock.setAttribute("admin", admin);
        this.mockMvc.perform(get("/admin/home").param("photo", "admin.png"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("courseCount"))
                .andExpect(view().name("ADM-DB001"));
    }

    @Test
    public void courseListTest() throws Exception {
        User admin = userObj();
        sessionMock.setAttribute("admin", admin);
        List<Course> courses=new ArrayList<>();
        Mockito.when(courseService.findAll()).thenReturn(courses);
        this.mockMvc.perform(get("/admin/course-list").session(sessionMock))
                .andExpect(status().isOk())
                .andExpect(view().name("ADM-CT001"));
    }

    @Test
    public void courseCreateTest() throws Exception {
        this.mockMvc.perform(post("/admin/course-create"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-CT001"));
    }

    @Test
    public void courseCreateNotNullTest() throws Exception {

    }
}
