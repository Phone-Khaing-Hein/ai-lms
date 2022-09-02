package com.ai.Controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources.Chain.Strategy.Content;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ai.service.*;

// import antlr.collections.List;
import java.util.List;
import com.ai.entity.*;
import com.ai.repository.*;

@SpringBootTest()
@AutoConfigureMockMvc

@WithMockUser(username = "ADMIN", password = "password", roles = "ADMIN")
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    CourseService courseService;

    @Autowired
    WebApplicationContext webApplicationContext;

    // @BeforeEach
    // public void mustTest() throws Exception{
    // this.mockMvc.perform(MockMvcRequestBuilders.get("/admin")
    // .with(SecurityMockMvcRequestPostProcessors.user("Admin User").roles("ADMIN"))
    // );
    // }

    @Configuration
    @EnableWebSecurity
    static class Config extends WebSecurityConfigurerAdapter{

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
    auth.inMemoryAuthentication().withUser("ADMIN").password("password").roles("ADMIN");
    }
    }


    @Test
    public void testHome() throws Exception {
    
        this.mockMvc.perform(get("/admin/home"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("courseCount"))
                .andExpect(view().name("ADM-DB001"));
    }

    @Test
    public void testCourseList() throws Exception {
        // Course course = new Course();
        // course.setId(1);
        // course.setModules(modules);
        List<Course> courses=new ArrayList<>();
        Mockito.when(courseService.findAll()).thenReturn(courses);
        this.mockMvc.perform(get("/admin/course-list"))
                .andExpect(status().isOk())
        
                .andExpect(view().name("ADM-CT001"));
    }
}
