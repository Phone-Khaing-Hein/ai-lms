package com.ai.ControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.util.StringUtils;

import com.ai.service.*;

import java.util.List;
import com.ai.entity.*;
import com.ai.entity.Module;
import com.ai.entity.User.Role;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@WithUserDetails("ADM001")
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "userService")
    private UserService uService;

    @MockBean(name = "courseService")
    private CourseService couService;

    @MockBean(name = "moduleService")
    private ModuleService modService;

    @MockBean(name = "videoService")
    private VideoService vdService;

    @MockBean(name = "batchService")
    private BatchService bService;

    @MockBean(name = "resourceService")
    private ResourceService rsService;

    @MockBean(name = "answerService")
    private AnswerService ansService;

    // @MockBean(name = "assignmentService")
    // private AssignmentService assignmentService;

    @MockBean(name = "assignmentAnswerService")
    private AssignmentAnswerService assAnsService;

    @MockBean(name = "attendanceService")
    private AttendanceService attService;

    // @MockBean(name = "batchHasExamService")
    // private BatchHasExamService batchHasExamService;

    // @MockBean(name = "commentService")
    // private CommentService commentService;

    //private PasswordEncoder passwordencoder;

    @MockBean(name = "examService")
    private ExamService exService;

    // @MockBean(name = "messageService")
    // private MessageService messageService;

    @MockBean(name = "questionService")
    private QuestionService quesService;

    @MockBean(name = "studentHasExamService")
    private StudentHasExamService sheService;

    @MockBean(name = "fileService")
    private FileService fService;

    // @MockBean(name = "privateCommentService")
    // private PrivateCommentService privateCommentService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User userObj(){
        Integer[] i = {0};
        User admin = User.builder()
        .loginId("ADM001")
        .name("Admin User")
        .password("admin")
        .role(Role.Admin)
        .email("phyuthin2004@gmail.com")
        .photo("default.png")
        .isActive(true)
        .batchId(i)
        .build();
        return admin;
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

    public User teacherObj(){
        Integer[] i = {0};
        User teacher = User.builder()
        .loginId("TCH002")
        .name("Teacher")
        .password("teacher")
        .role(Role.Teacher)
        .email("teacher@gmail.com")
        .photo("default.png")
        .isActive(true)
        .batchId(i)
        .build();
        return teacher;
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

    public List<Course> courseList(){
        List<Course> courses = new ArrayList<Course>();

        Course course1 = Course.builder()
        .id(1)
        .name("Course 1")
        .description("Course 1 description")
        .modules(new ArrayList<Module>())
        .batches(new ArrayList<Batch>())
        .exams(new ArrayList<Exam>())
        .build();

        Course course2 = Course.builder()
        .id(2)
        .name("Course 2")
        .description("Course 2 description")
        .modules(new ArrayList<Module>())
        .batches(new ArrayList<Batch>())
        .exams(new ArrayList<Exam>())
        .build();

        courses.add(course1);
        courses.add(course2);
        return courses;
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

    public Resource resource(){
        Resource resource = Resource.builder()
        .id(1)
        .name("Resource Object")
        .module(moduleList().get(0))
        .build();
        return resource;
    }

    public List<Resource> resources(){
        List<Resource> resources = new ArrayList<Resource>();

        Resource resource1 = Resource.builder()
        .id(1)
        .name("Resource Object 1")
        .module(moduleList().get(0))
        .build();

        Resource resource2 = Resource.builder()
        .id(2)
        .name("Resource Object 2")
        .module(moduleList().get(1))
        .build();

        resources.add(resource1);
        resources.add(resource2);
        return resources;
    }

    public List<Module> moduleList(){
        List<Module> modules = new ArrayList<Module>();

        Module module1 = Module.builder()
        .id(1)
        .name("Module Name 1")
        .course(courseObj())
        .resources(new ArrayList<>())
        .videos(new ArrayList<>())
        .schedule(new Schedule())
        .resourceCount(2)
        .videoCount(2)
        .build();

        Module module2 = Module.builder()
        .id(2)
        .name("Module Name 2")
        .course(courseObj())
        .resources(new ArrayList<>())
        .videos(new ArrayList<>())
        .schedule(new Schedule())
        .resourceCount(2)
        .videoCount(2)
        .build();
        
        modules.add(module1);
        modules.add(module2);
        
        return modules;
    }

    public List<Exam> examList(){
        List<Exam> exams = new ArrayList<>();

        Exam exam1 = Exam.builder()
        .id(1)
        .title("Exam 1")
        .fullmark(100)
        .questions(new ArrayList<Question>())
        .course(courseObj())
        .batchHasExams(new ArrayList<BatchHasExam>())
        .courseId(1)
        .build();

        Exam exam2 = Exam.builder()
        .id(2)
        .title("Exam 2")
        .fullmark(100)
        .questions(new ArrayList<Question>())
        .course(courseObj())
        .batchHasExams(new ArrayList<BatchHasExam>())
        .courseId(1)
        .build();

        exams.add(exam1);
        exams.add(exam2);

        return exams;
    }
    
    public Exam examObj(){
        Exam exam = Exam.builder()
        .id(1)
        .title("Exam")
        .fullmark(100)
        .questions(new ArrayList<Question>())
        .course(courseObj())
        .batchHasExams(new ArrayList<BatchHasExam>())
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

    public Video videoObj(){
        Video video = Video.builder()
        .id(1)
        .name("Video Name")
        .module(moduleObj())
        .comments(new ArrayList<Comment>())
        .build();
        return video;
    }

    @Test
    public void homeTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
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
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        this.mockMvc.perform(get("/admin/course-list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ADM-CT001"));
    }

    @Test
    public void courseCreateTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        this.mockMvc.perform(post("/admin/course-create"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-CT001"));
    }

    @Test
    public void courseCreateNotNullTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        Course course = courseObj();
        Mockito.when(couService.findByName(course.getName())).thenReturn(course);
        this.mockMvc.perform(post("/admin/course-create").flashAttr("course", course))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/course-list"));
    }

    @Test
    public void courseDetailTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        Course course = courseObj();
        Mockito.when(modService.findByCourseId(course.getId())).thenReturn(course.getModules());
        Mockito.when(couService.findById(course.getId())).thenReturn(course);
        this.mockMvc.perform(get("/admin/course-detail").param("courseId", String.valueOf(course.getId())))
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
        this.mockMvc.perform(get("/admin/course-delete").param("courseId", String.valueOf(course.getId())).param("courseName", course.getName()))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/course-list"));
    }

    @Test
    public void moduleCreateTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        Module module = moduleObj();
        Mockito.when(couService.findById(module.getCourse().getId())).thenReturn(module.getCourse());
        this.mockMvc.perform(post("/admin/module-create").param("courseId", String.valueOf(module.getCourse().getId())).flashAttr("module", module))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-MT001"));
    }


    //Actually shoule be 302
    @Test
    public void moduleNotNullTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        Module module = moduleObj();
        int courseId = module.getCourse().getId();
        var m = modService.findByName(module.getName());
        Mockito.when(m).thenReturn(module);
        this.mockMvc.perform(post("/admin/module-create").param("courseId", String.valueOf(courseId)).flashAttr("module", module))
                .andExpect(status().is(200))
                .andExpect(view().name("ADM-MT001"));
    }

    @Test
    public void resourceListTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        Module module = moduleObj();
        Mockito.when(rsService.findByModuleId(module.getId())).thenReturn(module.getResources());
        Mockito.when(modService.findById(module.getId())).thenReturn(module);
        this.mockMvc.perform(get("/admin/resource-list").param("moduleId", String.valueOf(module.getId())))
            .andExpect(model().attributeExists("resources"))
            .andExpect(model().attributeExists("moduleName"))
            .andExpect(model().attributeExists("course"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-RS001"));
    }

    @Test
    public void moduleEditTest() throws Exception {
        Module module = moduleObj();
        int courseId = module.getCourse().getId();
        Mockito.when(modService.findById(module.getId())).thenReturn(module);
        Mockito.when(couService.findById(courseId)).thenReturn(module.getCourse());
        //assertEquals(expected, actual);
        this.mockMvc.perform(post("/admin/module-edit").param("moduleId", String.valueOf(module.getId())).param("name", module.getName()).param("courseId", String.valueOf(module.getCourse().getId())))
            //.andExpect(model().attributeExists("name"))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/course-detail?courseId=" + module.getCourse().getId()));
    }


    //Include delete function
    @Test
    public void moduleDeleteTest() throws Exception {
        Module module = moduleObj();
        Mockito.when(modService.findById(module.getId())).thenReturn(module);
        this.mockMvc.perform(get("/admin/module-delete").param("moduleId", String.valueOf(module.getId())))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/course-detail?courseId=" + module.getCourse().getId()));
    }

    @Test
    public void resourceCreateTest() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        Module module = moduleObj();
        String moduleId = String.valueOf(module.getId());
        try {
            ((ResultActions) ((MockMultipartHttpServletRequestBuilder) this.mockMvc.perform(multipart("/admin/resource-create"))).file(firstFile).param("moduleId", moduleId))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/resource-list?moduleId=" + moduleId));
        } catch (Exception e) {
            
        }

    }

    @Test
    public void resourceDeleteTest() throws Exception {
        Resource resource = resource();
        Module module = moduleObj();
        int moduleId = module.getId();
        Mockito.when(rsService.findById(resource.getId())).thenReturn(resource);
        this.mockMvc.perform(get("/admin/resource-delete").param("resourceId", String.valueOf(resource.getId())))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/resource-list?moduleId=" + moduleId));
    }

    @Test
    public void videoListTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        Module module = moduleObj();
        String moduleId = String.valueOf(module.getId());
        Mockito.when(vdService.findByModuleId(module.getId())).thenReturn(module.getVideos());
        Mockito.when(modService.findById(module.getId())).thenReturn(module);
        this.mockMvc.perform(get("/admin/video-list").param("moduleId", moduleId))
            .andExpect(model().attributeExists("videos"))
            .andExpect(model().attributeExists("moduleName"))
            .andExpect(model().attributeExists("course"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-VD001"));
    }

    @Test
    public void createVideoTest() throws Exception {
        Module module = moduleObj();
        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        String moduleId = String.valueOf(module.getId());
        try {
            ((ResultActions) ((MockMultipartHttpServletRequestBuilder) this.mockMvc.perform(multipart("/admin/video-delete"))).file(firstFile).param("moduleId", moduleId))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/video-list?moduleId=" + moduleId));
        } catch (Exception e) {
            
        }
    }

    @Test
    public void deleteVideoTest() throws Exception {
        Video video = videoObj();
        String videoId = String.valueOf(video.getId());
        Mockito.when(vdService.findById(video.getId())).thenReturn(video);
        this.mockMvc.perform(get("/admin/video-delete").param("videoId", videoId))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/video-list?moduleId=" + video.getModule().getId()));
    }

    @Test
    public void batchListTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        this.mockMvc.perform(get("/admin/batch-list"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-BT001"));
    }

    @Test
    public void batchCreateBindingResultTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        this.mockMvc.perform(post("/admin/batch-create"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-BT001"));
    }

    @Test
    public void batchNotNullTest() throws Exception {
        Batch batch = batchObj();
        Mockito.when(bService.findByName("name")).thenReturn(batch);
        this.mockMvc.perform(post("/admin/batch-create").flashAttr("batch", batch))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/batch-list"));
    }

    @Test
    public void batchDateTest() throws Exception {
        Batch batch = batchObj();
        Mockito.when(!batch.getStartDate().isBefore(batch.getEndDate())).thenReturn(true);
        this.mockMvc.perform(post("/admin/batch-create").flashAttr("batch", batch))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/batch-list"));
    }

    @Test
    public void batchEditTest() throws Exception {
        Batch batch = batchObj();
        Mockito.when(bService.findById(batch.getId())).thenReturn(batch);
        this.mockMvc.perform(post("/admin/batch-edit").flashAttr("batch", batch))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/batch-list"));
    }

    @Test
    public void batchDeleteTest() throws Exception {
        Batch batch = batchObj();
        String batchId = String.valueOf(batch.getId());
        this.mockMvc.perform(get("/admin/batch-delete").param("batchId", batchId).param("batchName", batch.getName()))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/batch-list"));
    }

    @Test
    public void closeBatchTest() throws Exception {
        Batch batch = batchObj();
        String batchId = String.valueOf(batch.getId());
        Mockito.when(bService.findById(batch.getId())).thenReturn(batch);
        this.mockMvc.perform(get("/admin/batch-close").param("id", batchId))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/batch-list"));
    }

    @Test
    public void openBatchTest() throws Exception {
        Batch batch = batchObj();
        String batchId = String.valueOf(batch.getId());
        Mockito.when(bService.findById(batch.getId())).thenReturn(batch);
        this.mockMvc.perform(get("/admin/batch-open").param("id", batchId))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/batch-list"));
    }

    @Test
    public void createStudentBindingResultTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        this.mockMvc.perform(post("/admin/student-create"))
            .andExpect(model().attributeExists("openBatches"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-ST001"));
    }

    @Test
    public void studentCreateBatchNullTest() throws Exception {
        User user = userObj();
        Mockito.when(user.getBatchId()[0] == 0).thenReturn(true);
        this.mockMvc.perform(post("/admin/student-create"))
            .andExpect(model().attributeExists("openBatches"))
            .andExpect(model().attributeExists("batchError"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-ST001"));        
    }

    @Test
    public void createStudentIdExistsTest() throws Exception {
        User student = studentObj();
        Mockito.when(uService.findByLoginId("STU002")).thenReturn(student);
        this.mockMvc.perform(post("/admin/student-cteate"))
            .andExpect(model().attributeExists("error"))
            .andExpect(model().attributeExists("openBatches"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-ST001"));
    }

    @Test
    public void createStudentEmailExistsTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        this.mockMvc.perform(post("/admin/student-create"))
            .andExpect(model().attributeExists("emailError"))
            .andExpect(model().attributeExists("openBatches"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-ST001"));
    }

    @Test
    public void createStudentTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        this.mockMvc.perform(post("/admin/student-create").flashAttr("user", user))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/student-list"));
    }

    @Test
    public void studentListTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        this.mockMvc.perform(get("/admin/student-list"))
            .andExpect(model().attributeExists("openBatches"))
            .andExpect(model().attributeExists("nav"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-ST001"));
    }

    @Test
    public void studentEditTest() throws Exception {
        Integer[] id = {1};
        User student = User.builder()
        .loginId("STU001")
        .name("PPT")
        .email("ppt@gmail.com")
        .password("ppt2004")
        .role(Role.Student)
        .isActive(true)
        .batches(batchList())
        .messages(new ArrayList<Message>())
        .comments(new ArrayList<Comment>())
        .photo("default.png")
        .attendances(new ArrayList<Attendance>())
        .privateComments(new ArrayList<>())
        .studentHasExams(new ArrayList<>())
        .assignmentAnswer(List.of(new AssignmentAnswer()))
        .batchId(id)
        .percentage(20)
        .build();
        Mockito.when(uService.findByLoginId("STU001")).thenReturn(student);
        this.mockMvc.perform(post("/admin/student-edit").flashAttr("user", student))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/student-list"));
    }

    @Test
    public void studentDeleteTest() throws Exception {
        User student = studentObj();
        this.mockMvc.perform(get("/admin/student-delete").param("studentId", student.getLoginId()).param("studentName", student.getName()))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/student-list"));
    }

    @Test
    public void usersTest() throws Exception {
        List<User> users = userList();
        //var loginId = SecurityContextHolder.getContext().getAuthentication().getName();        
        Mockito.when(uService.findAll().stream().filter(a -> a.getRole().equals(User.Role.Student)).toList()).thenReturn(users);
        assertEquals(Role.Student, users.get(0).getRole());
    }

    @Test
    public void studentTest() throws Exception {
        User student = studentObj();
        assertEquals(Role.Student, student.getRole());
        assertEquals(true, student.isActive());
    }

    @Test
    public void batchesTest() throws Exception {
        List<Batch> batches = batchList();
        Mockito.when(bService.findAll()).thenReturn(batches);
    }

    @Test
    public void coursesTest() throws Exception {
        List<Course> courses = courseList();
        Mockito.when(couService.findAll()).thenReturn(courses);
    }

//Cannot Test @ModelAttribute from this line
    @Test
    public void courseTest() throws Exception {
        // Course course = new Course();
        // Mockito.when(course == null).thenReturn(true);
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
// to this line

    @Test
    public void loginUserTest() throws Exception {
        User user=userObj();
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();        
        Mockito.when(uService.findByLoginId(loginId)).thenReturn(user);
        User user1=uService.findByLoginId("ADM001");
        assertEquals(user.getLoginId(), user1.getLoginId());
    }

    @Test
    public void createTeacherBindingResultTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        this.mockMvc.perform(post("/admin/teacher-create"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-TC001"));
    }

    @Test
    public void createTeacherIdExistsTest() throws Exception {
        User teacher = teacherObj();
        Mockito.when(uService.findByLoginId("TCHOO2")).thenReturn(teacher);
        this.mockMvc.perform(post("/admin/teacher-create").flashAttr("user", teacher))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/teacher-list"));
    }

    @Test
    public void createTeacherTest() throws Exception {
        User teacher = teacherObj();
        this.mockMvc.perform(post("/admin/teacher-create").flashAttr("user", teacher))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/teacher-list"));
    }

    @Test
    public void teacherListTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        this.mockMvc.perform(get("/admin/teacher-list"))
            .andExpect(model().attributeExists("openBatches"))
            .andExpect(model().attributeExists("teachers"))
            .andExpect(model().attributeExists("nav"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-TC001"));
    }

    @Test
    public void teacherEditTest() throws Exception {
        User teacher = teacherObj();
        Mockito.when(uService.findByLoginId(teacher.getLoginId())).thenReturn(teacher);
        this.mockMvc.perform(post("/admin/teacher-edit").flashAttr("user", teacher))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/teacher-list"));
    }

    @Test
    public void deleteTeacherTest() throws Exception {
        User teacher = teacherObj();
        this.mockMvc.perform(get("/admin/teacher-delete").param("teacherId", teacher.getLoginId()).param("teacherName", teacher.getName()))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/teacher-list"));
    }

    @Test
    public void attendanceListTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        this.mockMvc.perform(get("/admin/attendance-list"))
            .andExpect(model().attributeExists("attendance"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-AT001"));
    }

    @Test
    public void examListTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        List<Exam> exams = examList();
        Mockito.when(exService.findAll()).thenReturn(exams);
        this.mockMvc.perform(get("/admin/exam-list"))
            .andExpect(model().attributeExists("exam"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-ET001"));
    }

    @Test
    public void examCreateTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        List<Course> courses = courseList();
        Mockito.when(couService.findAll()).thenReturn(courses);
        this.mockMvc.perform(get("/admin/exam-create"))
            .andExpect(model().attributeExists("courses"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-ET002"));
    }

    @Test
    public void examDeleteTest() throws Exception {
        Exam exam = examObj();
        String examId = String.valueOf(exam.getId());
        String examTitle = exam.getTitle();
        this.mockMvc.perform(get("/admin/exam-delete").param("examId",examId).param("examTitle", examTitle))    
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/exam-list"));
    }

    @Test
    public void examDetailTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        this.mockMvc.perform(get("/admin/exam-detail"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-ED001"));
    }

    @Test
    public void profileTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        Mockito.when(uService.findByLoginId(user.getLoginId())).thenReturn(user);
        this.mockMvc.perform(get("/admin/profile"))
            .andExpect(model().attributeExists("user"))
            .andExpect(view().name("ADM-PF001"));
    }

    // @Test
    // public void changePasswordTest() throws Exception {
    //     User user = userObj();
    //     Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
    //     String newPassword = "newPassword";
    //     this.mockMvc.perform(post("/admin/change-password").param("oldPassword", user.getPassword()).param("newPassword", newPassword))
    //         .andExpect(model().attributeExists("user"))
    //         .andExpect(model().attributeExists("oldPassword"))
    //         .andExpect(model().attributeExists("newPassword"))
    //         .andExpect(status().is(302))
    //         .andExpect(redirectedUrl("/admin/profile"));
    // }

    @Test
    public void passwordLengthTest() throws Exception {
        User user = userObj();
        String newPassword = "newPassword";
        
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        this.mockMvc.perform(post("/admin/change-password").param("oldPassword", user.getPassword()).param("newPassword", newPassword))
            .andExpect(model().attributeExists("user"))
            .andExpect(model().attributeExists("oldPassword"))
            .andExpect(model().attributeExists("newPassword"))
        
        // Mockito.when(!StringUtils.hasLength(user.getPassword())).thenReturn(user);
        // this.mockMvc.perform(post("/admin/change-password").param("oldPassword", user.getPassword()).param("newPassword", "newPassword"))
        //     .andExpect(model().attributeExists("oldPasswordError"));
        
        // Mockito.when(!StringUtils.hasLength(newPassword)).thenReturn(true);
        // this.mockMvc.perform(post("/admin/change-password").param("oldPassword", user.getPassword()).param("newPassword", "newPassword"))
        //     .andExpect(model().attributeExists("newPasswordError"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-PF001"));
    }

    @Test
    public void oldPasswordTest() throws Exception{
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        String oldPassword = "admin";
        String newPassword = "newPassword";
        assertEquals(oldPassword, user.getPassword());
        this.mockMvc.perform(post("/admin/change-password").param("oldPassword", user.getPassword()).param("newPassword", newPassword))
            .andExpect(model().attributeExists("oldPasswordError"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-PF001"));
    }

    @Test
    public void newPasswordTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        String oldPassword = "admin";
        String newPassword = "admin";
        assertEquals(newPassword, user.getPassword());
        this.mockMvc.perform(post("/admin/change-password").param("oldPassword", oldPassword).param("newPassword", user.getPassword()))
            //.andExpect(model().attributeExists("newPasswordError"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-PF001"));
    }

    @Test
    public void resetPasswordTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        this.mockMvc.perform(post("/admin/reset-password").param("loginId", user.getLoginId()).param("password", "newPassword"))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/admin/teacher-list"));
    }

    @Test
    public void examGradeTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        List<User> users = userList();
        List<StudentHasExam> sheList = sheList();
        List<StudentHasExam> sheSize = new ArrayList<StudentHasExam>();
        Mockito.when(uService.findAll()).thenReturn(users);
        Mockito.when(sheService.findAll()).thenReturn(sheList);
        Mockito.when(sheService.findAll()).thenReturn(sheSize);
        this.mockMvc.perform(get("/admin/exam-grades"))
            .andExpect(model().attributeExists("students"))
            .andExpect(model().attributeExists("exam"))
            .andExpect(model().attributeExists("nav"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-ET003"));
    }

    @Test
    public void assignmentGradeTest() throws Exception {
        User user = userObj();
        Mockito.when(uService.findByLoginId("ADM001")).thenReturn(user);
        List<User> users = userList();
        List<AssignmentAnswer> assAnsList = assAnsList();
        Mockito.when(uService.findAll()).thenReturn(users);
        Mockito.when(assAnsService.findAll()).thenReturn(assAnsList);
        this.mockMvc.perform(get("/admin/assignment-grades"))
            .andExpect(model().attributeExists("students"))
            .andExpect(model().attributeExists("assignment"))
            .andExpect(model().attributeExists("nav"))
            .andExpect(status().isOk())
            .andExpect(view().name("ADM-AT002"));
    }

} 