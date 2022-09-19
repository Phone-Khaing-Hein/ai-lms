package com.ai.ControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.ai.entity.Batch;
import com.ai.entity.User;
import com.ai.entity.User.Role;
import com.ai.repository.UserRepository;
import com.ai.service.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@ContextConfiguration
@WithUserDetails("TCH001")
public class TeacherControllerTest {
    
    @Resource
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @MockBean
    private CourseService courseService;

    @Mock
	MockHttpSession sessionMock;

    public List<Batch> batchObj(){
        List<Batch> batchList = new ArrayList<Batch>();
        Batch batch1 = Batch.builder()
        .id(1)
        .name("OJT Batch-1")
        .startDate(LocalDate.now())
        .endDate(LocalDate.now())
        .close(false)
        .build();
        Batch batch2 = Batch.builder()
        .id(2)
        .name("OJT Batch-2")
        .startDate(LocalDate.now())
        .endDate(LocalDate.now())
        .close(false)
        .build();
        batchList.add(batch1);
        batchList.add(batch2);
        return batchList;
    }
    public User userObj(){
        User user = User.builder()
        .loginId("TCH001")
        .name("Phyu Phyu Thin")
        .email("phyuthin2004@gmail.com")
        .password("password")
        .isActive(true)
        .role(Role.Teacher)
        .batches(batchObj())
        .build();
        return user;
    }
    @Test
    public void homeTest() throws Exception {
        this.mockMvc.perform(get("/teacher/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher/TCH-DB001"));
    }

    @Test
    public void batchListTest() throws Exception {
        this.mockMvc.perform(get("/teacher/batch-list"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("batches"))
            .andExpect(view().name("teacher/TCH-BT002"));
    }

    @Test
    public void batchDetailTest() throws Exception {
        this.mockMvc.perform(get("/teacher/batch-detail").param("batchId", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("batch"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("modules"))
                .andExpect(model().attributeExists("schedule"))
                .andExpect(model().attributeExists("attendance"))
                .andExpect(model().attributeExists("batchHasExam"))
                .andExpect(model().attributeExists("assignmentAnswers"))
                .andExpect(view().name("teacher/TCH-BD003"));
    }

    @Test
    public void createAttendance() throws Exception {
        this.mockMvc.perform(get("/teacher/createAttendance").param("batchId", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("attendance"))
                .andExpect(model().attributeExists("batch"))
                .andExpect(model().attributeExists("students"))
                .andExpect(view().name("teacher/TCH-AT001"));
    }

    @Test
    public void setAttendance() throws Exception {
        
    }

}
