package com.ai.ControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.xmlunit.diff.Comparison.Detail;

import com.ai.controller.TeacherController;
import com.ai.entity.Assignment;
import com.ai.entity.AssignmentAnswer;
import com.ai.entity.Attendance;
import com.ai.entity.Batch;
import com.ai.entity.BatchHasExam;
import com.ai.entity.Course;
import com.ai.entity.User;
import com.ai.entity.Video;
import com.ai.entity.Exam;
import com.ai.entity.Module;
import com.ai.entity.PrivateComment;
import com.ai.entity.Schedule;
import com.ai.entity.StudentHasExam;
import com.ai.entity.User.Role;
import com.ai.entity.Resource;
import com.ai.repository.BatchHasExamRepository;
import com.ai.repository.ScheduleRepository;
import com.ai.service.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@WithUserDetails("TCH001")
public class TeacherControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "courseService")
    private CourseService couService;

    @MockBean(name = "userService")
    private UserService uService;

    @MockBean(name = "moduleService")
    private ModuleService modService;

    @MockBean(name = "resourceService")
    private ResourceService rsService;

    @MockBean(name = "videoService")
    private VideoService vdService;

    @MockBean(name = "fileService")
    private FileService fService;

    @MockBean(name = "batchService")
    private BatchService bService;

    @MockBean(name = "scheduleRepository")
    private ScheduleRepository schRepo;

    @MockBean(name = "attendanceService")
    private AttendanceService attService;

    @MockBean(name = "assignmentService")
    private AssignmentService assService;

    @MockBean(name = "assignmentAnswerService")
    private AssignmentAnswerService assAnsService;

    @MockBean(name = "examService")
    private ExamService eService;

    @MockBean(name = "batchHasExamRepository")
    private BatchHasExamRepository bheRepo;

    @MockBean(name = "studentHasExamService")
    private StudentHasExamService sheService;

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
        List<Batch> batchList = new ArrayList<Batch>();
        
        Batch batch1 = Batch.builder()
        .id(1)
        .name("OJT Batch-1")
        .startDate(LocalDate.now())
        .endDate(LocalDate.now())
        .course(courseObj())
        .close(false)
        .build();

        Batch batch2 = Batch.builder()
        .id(2)
        .name("OJT Batch-2")
        .users(userList())
        .course(courseObj())
        .startDate(LocalDate.now())
        .endDate(LocalDate.now())
        .close(false)
        .build();

        batchList.add(batch1);
        batchList.add(batch2);

        return batchList;
    }

    public List<User> userList(){
        List<User> users = new ArrayList<User>();
        Integer[] i = {0};
        User admin = User.builder()
        .loginId("STU001")
        .name("Student")
        .password("student")
        .role(Role.Student)
        .email("phyuthin2004@gmail.com")
        .photo("default.png")
        .isActive(true)
        .batchId(i)
        .build();
        users.add(admin);
        return users;
    }

    public User userObj(){
        User user = User.builder()
        .loginId("TCH001")
        .name("Phyu Phyu Thin")
        .email("phyuthin2004@gmail.com")
        .password("password")
        .isActive(true)
        .role(Role.Teacher)
        .batches(batchList())
        .build();
        return user;
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

    public Attendance attendanceObj(){
        Attendance attendance = Attendance.builder()
        .id(1)
        .status("Present")
        .date(LocalDate.now())
        .user(studentObj())
        .batch(batchObj())
        .build();
        return attendance;
    }

    public User studentObj(){
        Integer[] i = {0};
        User student = User.builder()
        .loginId("STU002")
        .name("Student")
        .password("student")
        .role(Role.Student)
        .email("student@gmail.com")
        .photo("default.png")
        .isActive(true)
        .batchId(i)
        .build();
        return student;
    }

    public Schedule scheduleObj(){
        Schedule schedule = Schedule.builder()
        .id(1)
        .date(LocalDate.now())
        .batch(batchObj())
        .module(moduleObj())
        .batchId(1)
        .moduleId(1)
        .build();
        return schedule;
    }

    public Module moduleObj(){
        Module module = Module.builder()
        .id(1)
        .name("Module Name")
        .course(courseObj())        //new Course()
        .resources(new ArrayList<Resource>())
        .videos(new ArrayList<Video>())
        .schedule(new Schedule())
        .resourceCount(1)
        .videoCount(1)
        .build();
        return module;
    }

    public AssignmentAnswer assAnsObj(){
        AssignmentAnswer assAns = AssignmentAnswer.builder()
        .id(1)
        .answer("Answer 1")
        .user(studentObj())
        .created(LocalDateTime.now())
        .privateComments(new ArrayList<PrivateComment>())
        .assignment(new Assignment())
        .mark(100)
        .build();
        return  assAns;
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


    public Assignment assignmentObj(){
        Assignment assignment = Assignment.builder()
        .id(1)
        .title("Assignment One")
        .start(LocalDateTime.now())
        .end(LocalDateTime.now())
        .mark(100)
        .batch(batchObj())
        .assignmentAnswers(assAnsList())
        .batchId(1)
        .build();
        return assignment;
    }

    public Exam examObj(){
        Exam exam = Exam.builder()
        .id(1)
        .title("Exam Title")
        .fullmark(100)
        .questions(new ArrayList<>())
        .course(courseObj())
        .batchHasExams(new ArrayList<>())
        .studentHasExams(sheList())
        .courseId(1)
        .build();
        return exam;
    }

    public List<StudentHasExam> sheList(){
        List<StudentHasExam> sheList = new ArrayList<>();

        StudentHasExam she1 = StudentHasExam.builder()
        .id(1)
        .mark(100)
        .student(studentObj())
        .exam(examObj())
        .build();

        StudentHasExam she2 = StudentHasExam.builder()
        .id(2)
        .mark(100)
        .student(studentObj())
        .exam(examObj())
        .build();

        sheList.add(she1);
        sheList.add(she2);

        return sheList;
    }

    @Test
    public void batchListTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("TCH001")).thenReturn(user);
        Mockito.when(bService.findById(user.getBatches().get(0).getId())).thenReturn(user.getBatches().get(0));
        this.mockMvc.perform(get("/teacher/batch-list"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("batches"))
            .andExpect(view().name("teacher/TCH-BT002"));
    }

    @Test
    public void batchDetailTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("TCH001")).thenReturn(user);
        Batch batch = batchObj();
        Mockito.when(bService.findById(batch.getId())).thenReturn(batch);
        String batchId = String.valueOf(batch.getId());
        this.mockMvc.perform(get("/teacher/batch-detail").param("batchId", batchId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("batch"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("modules"))
                .andExpect(model().attributeExists("attendance"))
                .andExpect(model().attributeExists("batchHasExam"))
                .andExpect(model().attributeExists("assignmentAnswers"))
                .andExpect(view().name("teacher/TCH-BD003"));
    }

    @Test
    public void createAttendance() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("TCH001")).thenReturn(user);
        Batch batch = batchObj();
        Mockito.when(bService.findById(batch.getId())).thenReturn(batch);
        String batchId = String.valueOf(batch.getId());
        this.mockMvc.perform(get("/teacher/attendance-create").param("batchId", batchId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("batch"))
                .andExpect(model().attributeExists("students"))
                .andExpect(view().name("teacher/TCH-AT001"));
    }

    @Test
    public void attendanceEditTest() throws Exception {
        Batch batch = batchObj();
        String batchId = String.valueOf(batch.getId());
        Attendance attendance = attendanceObj();
        String attendanceId = String.valueOf(attendance.getId());
        Mockito.when(attService.findById(attendance.getId())).thenReturn(attendance);
        this.mockMvc.perform(post("/teacher/attendance-edit").param("batchId", batchId).param("status", attendance.getStatus()).param("attendanceId", attendanceId))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/teacher/batch-detail?batchId=" + batch.getId() + "#attendance-tab"));
    }

    @Test
    public void saveScheduleTest() throws Exception {
        Schedule schedule = scheduleObj();
        Module module = moduleObj();
        Mockito.when(modService.findById(1)).thenReturn(module);
        Mockito.when(schRepo.findById(schedule.getId())).thenReturn(Optional.of(schedule));
        this.mockMvc.perform(post("/teacher/schedule-create").flashAttr("schedule", schedule))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/teacher/batch-detail?batchId=" + schedule.getBatch().getId() + "#schedule-tab"));
    }

    @Test
    public void profileTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("TCH001")).thenReturn(user);
        this.mockMvc.perform(get("/teacher/profile"))
            .andExpect(model().attributeExists("user"))
            .andExpect(model().attributeExists("batches"))
            .andExpect(status().isOk())
            .andExpect(view().name("teacher/TCH-PF004"));
    }

    @Test
    public void passwordLengthTest() throws Exception {
        User user = userObj();
        String newPassword = "newPassword";        
        Mockito.when(uService.findByLoginId("TCH001")).thenReturn(user);
        this.mockMvc.perform(post("/teacher/change-password").param("oldPassword", user.getPassword()).param("newPassword", newPassword))
            .andExpect(model().attributeExists("user"))
            .andExpect(model().attributeExists("oldPassword"))
            .andExpect(model().attributeExists("newPassword"))
            .andExpect(status().isOk())
            .andExpect(view().name("teacher/TCH-PF004"));
    }

    @Test
    public void oldPasswordTest() throws Exception{
        User user = userObj();
        Mockito.when(uService.findByLoginId("TCH001")).thenReturn(user);
        String oldPassword = "password";
        String newPassword = "newPassword";
        assertEquals(oldPassword, user.getPassword());
        this.mockMvc.perform(post("/teacher/change-password").param("oldPassword", user.getPassword()).param("newPassword", newPassword))
            .andExpect(model().attributeExists("oldPasswordError"))
            .andExpect(status().isOk())
            .andExpect(view().name("teacher/TCH-PF004"));
    }

    @Test
    public void newPasswordTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("TCH001")).thenReturn(user);
        String oldPassword = "passwprd";
        String newPassword = "password";
        assertEquals(newPassword, user.getPassword());
        this.mockMvc.perform(post("/teacher/change-password").param("oldPassword", oldPassword).param("newPassword", user.getPassword()))
            //.andExpect(model().attributeExists("newPasswordError"))
            .andExpect(status().isOk())
            .andExpect(view().name("teacher/TCH-PF004"));
    }

    @Test
    public void chatTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("TCH001")).thenReturn(user);
        this.mockMvc.perform(get("/teacher/chat"))
            .andExpect(model().attributeExists("batches"))
            .andExpect(model().attributeExists("batch"))
            .andExpect(model().attributeExists("user"))
            .andExpect(status().isOk())
            .andExpect(view().name("teacher/TCH-CH007"));
    }

    @Test
    public void assignmentsTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("TCH001")).thenReturn(user);
        this.mockMvc.perform(get("/teacher/assignment-list"))
            .andExpect(model().attributeExists("assignments"))
            .andExpect(model().attributeExists("openBatches"))
            .andExpect(status().isOk())
            .andExpect(view().name("teacher/TCH-AL005"));
    }

    @Test
    public void assignmentCreateTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("TCH001")).thenReturn(user);
        AssignmentAnswer assAns = assAnsObj();
        String assAnsId = String.valueOf(assAns.getId());
        Mockito.when(assAnsService.findById(assAns.getId())).thenReturn(assAnsObj());
        this.mockMvc.perform(get("/teacher/assignment-detail").param("id", assAnsId))
            .andExpect(model().attributeExists("assignmentAnswer"))
            .andExpect(status().isOk())
            .andExpect(view().name("teacher/TCH-AD006"));
    }

    @Test
    public void studentTest() throws Exception {
        User user = userObj();
        assertEquals(Role.Teacher, user.getRole());
        assertEquals(true, user.isActive());
    }

    

    //setAttendance
    //exams
    //examSchedle
    //assignmentDelete
    //assignmentEdit
    // @Test
    // public void assignmentCheckTest() throws Exception {
    //     User user = userObj();
    //     Mockito.when(uService.findByLoginId("TCH001")).thenReturn(user);
    //     AssignmentAnswer assAns = assAnsObj();
    //     String assAnsMark = String.valueOf(assAns.getMark());
    //     String assAnsId = String.valueOf(assAns.getId());
    //     Mockito.when(assAnsService.findById(assAns.getId())).thenReturn(assAnsObj());
    //     Assignment assignment = assignmentObj();
    //     Mockito.when(assService.findById(1)).thenReturn(assignment);
    //     Mockito.when(assService.findById(assignment.getId()).getBatch()).thenReturn(assignment.getBatch());
    //     this.mockMvc.perform(post("/teacher/assignment-check").param("id", assAnsId).param("mark", assAnsMark))
    //         .andExpect(status().is(302))
    //         .andExpect(redirectedUrl("/teacher/batch-detail?batchId=" + assAns.getAssignment().getBatch().getId()));
    // }

    // @Test
    // public void homeTest() throws Exception {
    //     User user = userObj();
    //     List<Batch> batchList = batchList(); 
    //    // int batchId = teacher.getBatches().get(0).getId();
    //     Mockito.when(uService.findByLoginId("TCH001")).thenReturn(user);
    //     Mockito.when(bService.findById(1)).thenReturn(batchList.get(0));
    //     Mockito.when(bService.findById(user.getBatches().get(0).getId())).thenReturn(user.getBatches().get(0));
        
    //     this.mockMvc.perform(get("/teacher/home"))
    //             .andExpect(status().isOk())
    //             .andExpect(model().attributeExists("attendanceChart"))
    //             .andExpect(view().name("teacher/TCH-DB001"));
    // }

}
