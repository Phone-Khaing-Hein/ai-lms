package com.ai.controller;

import com.ai.entity.AssignmentAnswer;
import com.ai.entity.BatchHasExam;
import com.ai.entity.User;
import com.ai.repository.ScheduleRepository;
import com.ai.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.annotation.MultipartConfig;

@Controller
@RequestMapping("student")
@MultipartConfig
public class StudentController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BatchService batchService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private AssignmentAnswerService assignmentAnswerService;

    @Autowired
    private FileService fileService;

    @Autowired
    private BatchHasExamService batchHasExamService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("home")
    public String home(ModelMap m) {
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        Integer batchId = user.getBatches().get(0).getId();
        var batchCount = batchHasExamService.getBatchHasExamListByBatchId(batchId).stream().filter(e -> e. getStart() != null ? e.getStart().isBefore(LocalDateTime.now()) : e.getId() == -5).toList().size();
        m.put("batchCount", batchCount);
        var teachers = userService.findAll().stream()
                .filter(u -> u.getRole().equals(User.Role.Teacher))
                .filter(t -> t.getBatches().contains(batchService.findById(user.getBatches().get(0).getId()))).toList();
        m.put("user", user);
        m.put("teachers", teachers);
        m.put("assignmentCount", assignmentService.findAll().stream().filter(a -> a.getStart().isBefore(LocalDateTime.now())).toList().stream().filter(a -> a.getBatch().getId() == user.getBatches().get(0).getId()).toList().size());
        m.put("nav", "dashboard");
        return "student/STU-DB001";
    }

    @GetMapping("course")
    public String course(ModelMap m) {
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        var course = user.getBatches().get(0).getCourse();
        m.put("course", course);
        m.put("nav", "video");
        var modules = moduleService.findByCourseId(course.getId());
        for(var mo : modules){
            mo.setSchedule(scheduleRepository.findByBatch_IdAndModule_Id(user.getBatches().get(0).getId(), mo.getId()));
        }
        m.put("modules", modules.stream().filter(mo -> mo.getSchedule() != null ? mo.getSchedule().getDate().isBefore(LocalDate.now()) || mo.getSchedule().getDate().isEqual(LocalDate.now()) : mo.getId() == -5).toList());
        System.out.println("module count : " + modules.stream().filter(mo -> mo.getSchedule() != null ? mo.getSchedule().getDate().isAfter(LocalDate.now()) || mo.getSchedule().getDate().isEqual(LocalDate.now()) : mo.getId() == -5).toList().size());
        return "student/STU-VD002";
    }

    @GetMapping("members")
    public String members(ModelMap m) {
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        var members = batchService.findById(user.getBatches().get(0).getId()).getUsers();
        m.put("members", members.stream()
                .filter(s -> s.getRole().equals(User.Role.Student))
                .filter(u -> !u.getLoginId().equals(user.getLoginId())).toList());
        m.put("nav", "participants");
        return "student/STU-MB003";
    }

    @GetMapping("chat")
    public String chat(ModelMap m) {
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        m.put("batch", user.getBatches().get(0));
        m.put("user", user);
        m.put("nav", "chat");
        return "student/STU-CH004";
    }

    @GetMapping("profile")
    public String profile(ModelMap m) {
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        m.put("user", user);
        return "student/STU-PF005";
    }

    @PostMapping("change-password")
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, ModelMap m,
            RedirectAttributes attributes) {

        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        m.put("user", user);
        m.put("oldPassword", oldPassword);
        m.put("newPassword", newPassword);

        if (!StringUtils.hasLength(oldPassword)) {
            m.put("oldPasswordError", "Old Password is required!");
            return "student/STU-PF005";
        }
        if (!StringUtils.hasLength(newPassword)) {
            m.put("newPasswordError", "New Password is required!");
            return "student/STU-PF005";
        }
        if (oldPassword.equals(newPassword)) {
            m.put("newPasswordError", "Old Password and New Password are same!");
            return "student/STU-PF005";
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            m.put("oldPasswordError", "Old Password is incorrect!");
            return "student/STU-PF005";
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.save(user);
        attributes.addFlashAttribute("message", "Changed Password successfully!");
        return "redirect:/student/profile";
    }

    // @PostMapping("profile-change")
    // public String profileChange(@RequestParam MultipartFile photo) throws
    // IOException {
    // var loginId =
    // SecurityContextHolder.getContext().getAuthentication().getName();
    // var user = userService.findByLoginId(loginId);
    // if(StringUtils.hasLength(user.getPhoto()) &&
    // !user.getPhoto().equals("default.png")){
    // var profileFilePath = new
    // File("src\\main\\resources\\static\\profile\\").getAbsolutePath();
    // fileService.deleteFile(profileFilePath.concat("\\").concat(user.getPhoto()));
    // }
    // var fileName = fileService.createProfileFile(photo, user.getLoginId());
    // user.setPhoto(fileName);
    // userService.save(user);
    // return "redirect:/student/profile";
    // }

    @GetMapping("exam-list")
    public String exams(ModelMap model) {
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        User student = userService.findByLoginId(loginId);
        Integer batchId = student.getBatches().get(0).getId();
        List<BatchHasExam> bheList = batchHasExamService.getBatchHasExamListByBatchId(batchId);
        model.addAttribute("bheList", bheList.stream().filter(e -> e. getStart() != null ? e.getStart().isBefore(LocalDateTime.now()) : e.getId() == -5).toList());
        return "student/STU-EX006";
    }

    @GetMapping("assignment-list")
    public String assignments(ModelMap m) {
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        User student = userService.findByLoginId(loginId);
        m.put("assignments",
                assignmentService.findAll().stream().filter(a -> a.getStart().isBefore(LocalDateTime.now())).filter(a -> a.getBatch().getId() == student.getBatches().get(0).getId()).toList());
        return "student/STU-AS007";
    }

    @GetMapping("assignment-detail")
    public String assignmentDetail(@RequestParam int id, ModelMap m) {
        m.put("assignment", assignmentService.findById(id));
        return "student/STU-AD008";
    }

    @GetMapping("delete-comment")
    public String deleteComment(@RequestParam int commentId, RedirectAttributes attributes) {
        commentService.delete(commentId);
        attributes.addFlashAttribute("message", "Your comment deleted successfully!");
        return "redirect:/student/course";
    }

    @PostMapping("assignment-submit")
    public String assignmentSubmit(@RequestParam(required = false) MultipartFile file, @RequestParam int id,
            RedirectAttributes attributes) throws IllegalStateException, IOException {
        var assignment = assignmentService.findById(id);
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        var answer = new AssignmentAnswer();
        answer.setUser(user);
        answer.setAssignment(assignment);
        fileService.createFolderForAssignmentAnswer();
        var fileName = fileService.createAssignmentAnswerFile(file);
        answer.setAnswer(fileName);
        answer.setCreated(LocalDateTime.now());
        assignmentAnswerService.save(answer);
        attributes.addFlashAttribute("message", "Your answer is submitted successfully!");
        return "redirect:/student/assignment-list";
    }

    @GetMapping("exam-detail/{bheId}")
    public String examDetail(@PathVariable("bheId") Integer bheId, ModelMap model) {
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        BatchHasExam bhe = batchHasExamService.getBatchHasExamById(bheId);
        model.addAttribute("bheId", bheId);
        model.addAttribute("studentId", loginId);
        model.addAttribute("examId", bhe.getExam().getId());
        return "student/STU-EF009";
    }

    @ModelAttribute("user")
    public User user() {
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByLoginId(loginId);
    }
    // @ModelAttribute("messages")
    // public List<Message> messagesView(){
    // var loginId =
    // SecurityContextHolder.getContext().getAuthentication().getName();
    // var user = userService.findByLoginId(loginId);
    // return messageService.findAll();
    // }
}
