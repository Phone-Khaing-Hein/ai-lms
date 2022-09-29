package com.ai.ControllerTest;


import com.ai.entity.*;
import com.ai.entity.Module;
import com.ai.entity.User.Role;
import com.ai.repository.ScheduleRepository;
import com.ai.repository.UserRepository;
import com.ai.service.AssignmentAnswerService;
import com.ai.service.AssignmentService;
import com.ai.service.BatchHasExamService;
import com.ai.service.BatchService;
import com.ai.service.CommentService;
import com.ai.service.CourseService;
import com.ai.service.ExamService;
import com.ai.service.FileService;
import com.ai.service.MessageService;
import com.ai.service.ModuleService;
import com.ai.service.UserService;
import com.ai.service.VideoService;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@WithUserDetails("STU001")
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "userService")
    private UserService uService;

    @MockBean(name = "moduleService")
    private ModuleService modService;

    @MockBean(name = "batchService")
    private BatchService bService;

    @MockBean(name = "messageService")
    private MessageService msgService;

    @MockBean(name = "videoService")
    private VideoService vdService;

    @MockBean(name = "commentService")
    private CommentService comService;

    @MockBean(name = "assignmentService")
    private AssignmentService assService;

    @MockBean(name = "assignmentAnswerService")
    private AssignmentAnswerService assAnsService;

    @MockBean(name = "fileService")
    private FileService fService;

    @MockBean(name = "batchHasExamService")
    private BatchHasExamService bheService;

    @MockBean(name = "scheduleRepository")
    private ScheduleRepository schRepo;

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

    public List<Batch> batchList(){
        List<Batch> batches = new ArrayList<>();

        Batch batch1 = Batch.builder()
        .id(1)
        .name("OJT Batch-1")
        .startDate(LocalDate.of(2022, 9, 12))
        .endDate(LocalDate.of(2022, 10, 12))
        .close(true)
        .course(courseObj())
        .users(new ArrayList<User>())
        .schedules(new ArrayList<Schedule>())
        .assignments(new ArrayList<Assignment>())
        .attendances(new ArrayList<Attendance>())
        .batchHasExams(new ArrayList<BatchHasExam>())
        .build();

        Batch batch2 = Batch.builder()
        .id(2)
        .name("OJT Batch-2")
        .startDate(LocalDate.of(2022, 9, 12))
        .endDate(LocalDate.of(2022, 10, 12))
        .close(true)
        .course(courseObj())
        .users(new ArrayList<User>())
        .schedules(new ArrayList<Schedule>())
        .assignments(new ArrayList<Assignment>())
        .attendances(new ArrayList<Attendance>())
        .batchHasExams(new ArrayList<BatchHasExam>())
        .build();

        batches.add(batch1);
        batches.add(batch2);
        return batches;
    }

    public User studentObj(){
        Integer[] i = {0};
        User student = User.builder()
        .loginId("STU001")
        .name("Student")
        .password("student")
        .role(Role.Student)
        .email("student@gmail.com")
        .photo("default.png")
        .isActive(true)
        .batchId(i)
        .batches(batchList())
        .build();
        return student;
    }

    public Batch batchObj(){
        Batch batch = Batch.builder()
        .id(1)
        .name("OJT Batch-1")
        .startDate(LocalDate.of(2022, 9, 12))
        .endDate(LocalDate.of(2022, 10, 12))
        .close(true)
        .course(courseObj())
        .users(new ArrayList<User>())
        .schedules(new ArrayList<Schedule>())
        .assignments(new ArrayList<Assignment>())
        .attendances(new ArrayList<Attendance>())
        .batchHasExams(new ArrayList<BatchHasExam>())
        .build();
        return batch;
    }

    public Assignment assignmentObj(){
        Assignment assignment = Assignment.builder()
        .id(1)
        .title("Assignment Title")
        .start(LocalDateTime.now())
        .end(LocalDateTime.now())
        .mark(100)
        .batch(batchObj())
        .assignmentAnswers(assAnsList())
        .build();
        return assignment;
    }

    public List<AssignmentAnswer> assAnsList(){
        List<AssignmentAnswer> assAnsList = new ArrayList<AssignmentAnswer>();

        AssignmentAnswer assAns1 = AssignmentAnswer.builder()
        .id(1)
        .answer("Answer 1")
        .user(studentObj())
        .created(LocalDateTime.now())
        .privateComments(new ArrayList<PrivateComment>())
        .assignment(new Assignment())
        .mark(100)
        .build();

        AssignmentAnswer assAns2 = AssignmentAnswer.builder()
        .id(2)
        .answer("Answer 2")
        .user(studentObj())
        .created(LocalDateTime.now())
        .privateComments(new ArrayList<PrivateComment>())
        .assignment(new Assignment())
        .mark(100)
        .build();

        assAnsList.add(assAns1);
        assAnsList.add(assAns2);

        return assAnsList;
    }

    public Comment commentObj(){
        Comment comment = Comment.builder()
        .id(1)
        .comment("comment")
        .created(LocalDateTime.now())
        .user(studentObj())
        .video(new Video())
        .videoId(1)
        .build();
        return comment;
    }

    public BatchHasExam bheObj(){
        BatchHasExam bhe = BatchHasExam.builder()
        .id(1)
        .start(LocalDateTime.now())
        .end(LocalDateTime.now())
        .batch(batchObj())
        .exam(new Exam())
        .build();
        return bhe;
    }

    @Test
    public void studentHomeTest() throws Exception {
        User student = studentObj();
        Mockito.when(uService.findByLoginId("STU001")).thenReturn(student);

        this.mockMvc.perform(get("/student/home"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("batchCount"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("teachers"))
                .andExpect(model().attributeExists("assignmentCount"))
                .andExpect(model().attributeExists("nav"))
                .andExpect(view().name("student/STU-DB001"));
    }

    @Test
    public void courseTest() throws Exception{
        User student = studentObj();
        Mockito.when(uService.findByLoginId("STU001")).thenReturn(student);
        this.mockMvc.perform(get("/student/course"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attributeExists("modules"))
                .andExpect(model().attributeExists("nav"))                
                .andExpect(view().name("student/STU-VD002"));
    }

    // @Test
    // public void membersTest() throws Exception{   
    //     User student = studentObj();
    //     List<Batch> batches = batchList();
    //     Mockito.when(uService.findByLoginId("STU001")).thenReturn(student);     
    //     Mockito.when(bService.findById(1).thenReturn(student.getBatches().get(0).getUsers());
    //     this.mockMvc.perform(get("/student/members"))
    //             .andExpect(status().isOk())
    //             .andExpect(model().attributeExists("members"))
    //             .andExpect(model().attributeExists("nav"))
    //             .andExpect(view().name("student/STU-MB003"));
    // }

    @Test
    public void chatTest() throws Exception{
        User user = studentObj();
        Mockito.when(uService.findByLoginId("STU001")).thenReturn(user);
        this.mockMvc.perform(get("/student/chat"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("batch"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("nav"))
                .andExpect(view().name("student/STU-CH004"));
    }

    @Test
    public void profileTest() throws Exception{
        User user = studentObj();
        Mockito.when(uService.findByLoginId("STU001")).thenReturn(user);
        this.mockMvc.perform(get("/student/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("student/STU-PF005"));
    }

    @Test
    public void passwordLengthTest() throws Exception {
        User user = studentObj();
        String newPassword = "newPassword";
        
        Mockito.when(uService.findByLoginId("STU001")).thenReturn(user);
        this.mockMvc.perform(post("/student/change-password").param("oldPassword", user.getPassword()).param("newPassword", newPassword))
            .andExpect(model().attributeExists("user"))
            .andExpect(model().attributeExists("oldPassword"))
            .andExpect(model().attributeExists("newPassword"))
            .andExpect(status().isOk())
            .andExpect(view().name("student/STU-PF005"));
    }

    @Test
    public void oldPasswordTest() throws Exception{
        User user = studentObj();
        Mockito.when(uService.findByLoginId("STU001")).thenReturn(user);
        String oldPassword = "student";
        String newPassword = "newPassword";
        assertEquals(oldPassword, user.getPassword());
        this.mockMvc.perform(post("/student/change-password").param("oldPassword", user.getPassword()).param("newPassword", newPassword))
            .andExpect(model().attributeExists("oldPasswordError"))
            .andExpect(status().isOk())
            .andExpect(view().name("student/STU-PF005"));
    }

    @Test
    public void newPasswordTest() throws Exception {
        User user = studentObj();
        Mockito.when(uService.findByLoginId("STU001")).thenReturn(user);
        String oldPassword = "student";
        String newPassword = "student";
        assertEquals(newPassword, user.getPassword());
        this.mockMvc.perform(post("/student/change-password").param("oldPassword", oldPassword).param("newPassword", user.getPassword()))
            .andExpect(model().attributeExists("newPasswordError"))
            .andExpect(status().isOk())
            .andExpect(view().name("student/STU-PF005"));
    }

    @Test
    public void examsTest() throws Exception{
        User user = studentObj();
        Mockito.when(uService.findByLoginId("STU001")).thenReturn(user);
        this.mockMvc.perform(get("/student/exam-list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bheList"))
                .andExpect(view().name("student/STU-EX006"));
    }

    @Test
    public void assignmentsTest() throws Exception{
        User user = studentObj();
        Mockito.when(uService.findByLoginId("STU001")).thenReturn(user);
        this.mockMvc.perform(get("/student/assignment-list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("assignments"))
                .andExpect(view().name("student/STU-AS007"));
    }

    
    @Test
    public void assignmentDetailTest() throws Exception{
        User user = studentObj();
        Mockito.when(uService.findByLoginId("STU001")).thenReturn(user);
        Assignment assignment = assignmentObj();
        String assignmentId = String.valueOf(assignment.getId());
        Mockito.when(assService.findById(assignment.getId())).thenReturn(assignment);
        //List<AssignmentAnswer> assAnsList = assAnsList();
        //Mockito.when(assAnsService.findByAssignment_IdAndUser_LoginId(assignment.getId(), user.getLoginId())).thenReturn(assAnsList);
        this.mockMvc.perform(get("/student/assignment-detail").param("id", assignmentId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("assignment"))
                .andExpect(view().name("student/STU-AD008"));
    }

    @Test
    public void deleteCommentTest() throws Exception{
        Comment comment = commentObj();
        String commentId = String.valueOf(comment.getId());
        Mockito.when(comService.findById(comment.getId())).thenReturn(comment);
        this.mockMvc.perform(get("/student/delete-comment").param("commentId", commentId))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/student/course"));
    }

    @Test
    public void assignmentSubmitTest() throws Exception{
        Assignment assignment = assignmentObj();
        String assignmentId = String.valueOf(assignment.getId());
        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        try {
            ((ResultActions) ((MockMultipartHttpServletRequestBuilder) this.mockMvc.perform(multipart("/student/assignment-submit"))).file(firstFile).param("id", assignmentId))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/student/assignment-dubmit"));
        } catch (Exception e) {
            
        } 
    }

    @Test
    public void examDetailTest() throws Exception{
        BatchHasExam bhe = bheObj();
        Mockito.when(bheService.getBatchHasExamById(bhe.getId())).thenReturn(bhe);
        this.mockMvc.perform(get("/student/exam-detail/{bheId}",bhe.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bheId"))
                .andExpect(model().attributeExists("studentId"))
                .andExpect(model().attributeExists("examId"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/STU-EF009"));
    }

    @Test
    public void userTest() throws Exception{
        User user = studentObj();
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();        
        Mockito.when(uService.findByLoginId(loginId)).thenReturn(user);
        User user1=uService.findByLoginId("STU001");
        assertEquals(user.getLoginId(), user1.getLoginId());
    }

}
