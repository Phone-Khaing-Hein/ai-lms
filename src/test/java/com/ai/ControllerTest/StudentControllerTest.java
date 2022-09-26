package com.ai.ControllerTest;


import com.ai.entity.*;
import com.ai.entity.Module;
import com.ai.repository.UserRepository;
import com.ai.service.AssignmentService;
import com.ai.service.BatchHasExamService;
import com.ai.service.BatchService;
import com.ai.service.CommentService;
import com.ai.service.CourseService;
import com.ai.service.ExamService;
import com.ai.service.ModuleService;
import com.ai.service.UserService;
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

import javax.annotation.Resource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)

@AutoConfigureMockMvc
@ContextConfiguration
@WithUserDetails("STU001")
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "bean1")
    private UserService userService;

    @MockBean(name = "bean2")
    private CourseService courseService;

    @MockBean(name = "bean3")
    private ModuleService moduleService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private AssignmentService assignmentService;

    @MockBean
    private BatchHasExamService batchHasExamService;

    @MockBean
    private ExamService examService;

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
                .loginId("STU001")
                .name("Swam Thu Marn")
                .email("swamthumarn@gmail.com")
                .password("password")
                .isActive(true)
                .role(User.Role.Student)
                .batches(batchObj())
                .build();
        return user;
    }

//    public Course courseObj(){
//        Course course=Course.builder()
//                .id(1)
//                .name("Java")
//                .description("Advanced Java Course")
//
//                return course;
//    }

    @Test
    public void studentHomeTest() throws Exception {
        this.mockMvc.perform(get("/student/home"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("batchCount"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("teachers"))
                .andExpect(model().attributeExists("assignmentCount"))
                .andExpect(model().attributeExists("nav"))
                .andExpect(model().attributeExists("progress"))
                .andExpect(view().name("student/STU-DB001"));
    }

    @Test
    public void courseTest() throws Exception{
        this.mockMvc.perform(get("/student/course"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attributeExists("modules"))
                .andExpect(model().attributeExists("nav"))                
                .andExpect(view().name("student/STU-VD002"));
    }

    @Test
    public void membersTest() throws Exception{        
        this.mockMvc.perform(get("/student/members"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("members"))
                .andExpect(model().attributeExists("nav"))
                .andExpect(view().name("student/STU-MB003"));
    }

    @Test
    public void chatTest() throws Exception{
        this.mockMvc.perform(get("/student/chat"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("batch"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("nav"))
                .andExpect(view().name("student/STU-CH004"));
    }

    @Test
    public void profileTest() throws Exception{
        this.mockMvc.perform(get("/student/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("student/STU-PF005"));
    }

    @Test
    public void changePasswordTest() throws Exception{

    }

    @Test
    public void examsTest() throws Exception{
        this.mockMvc.perform(get("/student/exam-list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bheList"))
                .andExpect(view().name("student/STU-EX006"));
    }

    @Test
    public void assignmentsTest() throws Exception{
        this.mockMvc.perform(get("/student/assignment-list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("assignments"))
                .andExpect(view().name("student/STU-AS007"));
    }

    
    // @Test
    // public void assignmentDetailTest() throws Exception{
    //     when(assignmentService.findById(1)).thenReturn(new Assignment());
    //     this.mockMvc.perform(get("/student/assignment-detail").param("id","1"))
    //             .andExpect(status().isOk())
    //             .andExpect(model().attributeExists("assignment"))
    //             .andExpect(view().name("student/STU-AD008"));
    // }

    @Test
    public void deleteCommentTest() throws Exception{
        when(commentService.findById(1)).thenReturn(new Comment());
        this.mockMvc.perform(get("/student/delete-comment").param("commentId", "1"))
                .andExpect(status().is(302))
                .andExpect(flash().attributeExists("message"))
                .andExpect(redirectedUrl("/student/course"));
    }

    // @Test
    // public void assignmentSubmitTest() throws Exception{
    //     this.mockMvc.perform(get("/student/assignment-submit"))
    //             .andExpect(status().is(302))
    //             .andExpect(flash().attributeExists("message"))
    //             .andExpect(redirectedUrl("student/assignment-list"));
    // }

    // @Test
    // public void examDetailTest() throws Exception{
    //     Integer batchHasExamId=1;        
    //     when(batchHasExamService.getBatchHasExamById(batchHasExamId)).thenReturn(new BatchHasExam());
    //     this.mockMvc.perform(get("/student/exam-detail/{bheId}",batchHasExamId))
    //             .andExpect(status().isOk())
    //             .andExpect(model().attributeExists("bheId"))
    //             .andExpect(model().attributeExists("studentId"))
    //             .andExpect(model().attributeExists("examId"))
    //             .andExpect(redirectedUrl("student/STU-EF009"));
    // }

    @Test
    public void userTest() throws Exception{
        User user=userObj();
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();        
        Mockito.when(userService.findByLoginId(loginId)).thenReturn(user);
        User user1=userService.findByLoginId("STU001");
        assertEquals(user.getLoginId(), user1.getLoginId());
    }

}
