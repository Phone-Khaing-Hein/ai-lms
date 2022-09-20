package com.ai.controller;

import com.ai.entity.Assignment;
import com.ai.entity.Attendance;
import com.ai.entity.BatchHasExam;
import com.ai.entity.Exam;
import com.ai.entity.Module;
import com.ai.entity.Schedule;
import com.ai.entity.StudentHasExam;
import com.ai.entity.User;
import com.ai.repository.BatchHasExamRepository;
import com.ai.repository.ScheduleRepository;
import com.ai.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

@Controller
@RequestMapping("teacher")
public class TeacherController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private FileService fileService;

    @Autowired
    private BatchService batchService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private AssignmentAnswerService assignmentAnswerService;

    @Autowired
    private ExamService examService;

    @Autowired
    private BatchHasExamRepository batchHasExamRepository;

    @Autowired
    private StudentHasExamService studentHasExamService;

    @GetMapping("home")
    public String home(){
        return "teacher/TCH-DB001";
    }

    @GetMapping("batch-list")
    public String batchList(ModelMap m){
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var batches = userService.findByLoginId(loginId).getBatches();
        m.put("batches", batches);
        return "teacher/TCH-BT002";
    }

    @GetMapping("batch-detail")
    public String batchDetail(@RequestParam int batchId, ModelMap m){
        var batch = batchService.findById(batchId);
        m.put("batch", batch);
        m.put("students", batch.getUsers().stream().filter(u -> u.getRole().equals(User.Role.Student)).toList());
        m.put("modules", batch.getCourse().getModules());
        m.put("schedule", scheduleRepository.findAll().stream().filter(s -> s.getBatch().getId() == batchId).toList());
        m.put("attendance", attendanceService.findAllAttendanceByBatchId(batchId));
        m.put("batchHasExam", batchHasExamRepository.findByBatchId(batchId));
        m.put("assignmentAnswers", assignmentAnswerService.findAll().stream().filter(a -> a.getUser().getBatches().get(0).getId() == batch.getId()).toList());
        return "teacher/TCH-BD003";
    }

    @GetMapping("attendance-create")
    public String createAttendance(@RequestParam int batchId,
                                   ModelMap m) {
        var batch = batchService.findById(batchId);
        m.put("batch", batch);
        m.put("students", batch.getUsers().stream().filter(u -> u.getRole().equals(User.Role.Student)).toList());
        return "teacher/TCH-AT001";
    }
    
    @PostMapping("attendance-create")
    public String setAttendance(
            @RequestParam int batchId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam List<String> status,
            @RequestParam List<String> loginId,
            RedirectAttributes attribute,
            ModelMap m) {

        var batch = batchService.findById(batchId);
    
        if(date == null){
            m.put("dateError","Attendance date is required!");
            m.put("batch", batch);
            m.put("students", batch.getUsers().stream().filter(u -> u.getRole().equals(User.Role.Student)).toList());
            return "teacher/TCH-AT001";
        }

        var attendanceForDate = attendanceService.findByDate(date);

        if(attendanceForDate == null){
            for (var i = 0; i < status.size(); i++) {
                for (var j = i; j < loginId.size(); ) {
                    var attendance = new Attendance();
                    attendance.setStatus(status.get(i));
                    attendance.setBatch(batch);
                    attendance.setDate(date);
                    attendance.setUser(userService.findByLoginId(loginId.get(j)));
                    attendanceService.save(attendance);
                    break;
                }
            }
            attribute.addFlashAttribute("success","Attendance for %s created successfully!".formatted(date.toString()));
            return "redirect:/teacher/batch-detail?batchId=%d#attendance-tab".formatted(batchId);
        }else{
            m.put("dateError", "Attendance for %s has already existed!".formatted(date.toString()));
            m.put("batch", batch);
            m.put("date", date);
            m.put("students", batch.getUsers().stream().filter(u -> u.getRole().equals(User.Role.Student)).toList());
            return "teacher/TCH-AT001";
        }
    }

    @PostMapping("attendance-edit")
    public String attendanceEdit(@RequestParam int batchId, @RequestParam String status, @RequestParam int attendanceId){
        var attendance = attendanceService.findById(attendanceId);
        attendance.setStatus(status);
        attendanceService.save(attendance);
        return "redirect:/teacher/batch-detail?batchId=%d#attendance-tab".formatted(batchId);
    }


    @PostMapping("schedule-create")
    public String saveSchedule(@Validated @ModelAttribute Schedule schedule, BindingResult bs, RedirectAttributes attributes){

        if(bs.hasErrors()){
            attributes.addFlashAttribute("dateError", "Schedule date is required!");
            return "redirect:/teacher/batch-detail?batchId=%d#schedule-tab".formatted(schedule.getBatchId());
        }
        var module = moduleService.findById(schedule.getModuleId());    
        if(schedule.getId() != 0){
            var s = scheduleRepository.findById(schedule.getId()).get();
            s.setDate(schedule.getDate());
            scheduleRepository.save(s);
        }else{
            schedule.setModule(module);
            schedule.setBatch(batchService.findById(schedule.getBatchId()));
            scheduleRepository.save(schedule);
        }
        attributes.addFlashAttribute("message", "%s scheduled successfully!".formatted(module.getName()));
        return "redirect:/teacher/batch-detail?batchId=%d#schedule-tab".formatted(schedule.getBatchId());
    }

//----------------------------------Profile---------------------------------------------------------------

    @GetMapping("profile")
    public String profile(ModelMap m){
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        m.put("user", user);
        m.put("batches", user.getBatches());
        return "teacher/TCH-PF004";
    }

    @PostMapping("change-password")
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, ModelMap m, RedirectAttributes attributes){

        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        m.put("user", user);
        m.put("oldPassword", oldPassword);
        m.put("newPassword", newPassword);

        if(!StringUtils.hasLength(oldPassword)){
            m.put("oldPasswordError", "Old Password is required!");
            if(!StringUtils.hasLength(newPassword)){
                m.put("newPasswordError", "New Password is required!");
                return "teacher/TCH-PF004";
            }
            return "teacher/TCH-PF004";
        }
        if(!StringUtils.hasLength(newPassword)){
            m.put("newPasswordError", "New Password is required!");
            return "teacher/TCH-PF004";
        }
        if(oldPassword.equals(newPassword)){
            m.put("newPasswordError", "Old Password and New Password are same!");
            return "teacher/TCH-PF004";
        }

        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            m.put("oldPasswordError", "Old Password is incorrect!");
            return "teacher/TCH-PF004";
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.save(user);
        attributes.addFlashAttribute("message","Changed Password successfully!");
        return "redirect:/teacher/profile";
    }

    @GetMapping("chat")
    public String chat(){
        return "teacher/TCH-CH007";
    }

    @GetMapping("assignment-list")
    public String assignments(ModelMap m){
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        m.put("assignments", assignmentService.findAll().stream().filter(a -> user.getBatches().contains(a.getBatch())).toList());
        m.put("openBatches", user.getBatches().stream().filter(b -> b.isClose() == false).toList());
        return "teacher/TCH-AL005";
    }

    @PostMapping("assignment-create")
    public String createAssignment(@Validated @ModelAttribute Assignment assignment, 
                                    BindingResult bs, 
                                    RedirectAttributes attributes) throws IllegalStateException, IOException{
        if(bs.hasErrors()){
            return "teacher/TCH-AL005";
        }
        var batch = batchService.findById(assignment.getBatchId());
        if(!assignment.getFiles().isEmpty()){
            fileService.createFolderForAssignment(batch.getName());
            var fileName = fileService.createAssignmentFile(assignment.getFiles(), batch.getName());
            assignment.setFile(fileName);
        }
        assignment.setBatch(batch);
        assignmentService.save(assignment);
        attributes.addFlashAttribute("message", "%s created successfully!".formatted(assignment.getTitle()));
        return "redirect:/teacher/assignment-list";
    }

    @GetMapping("assignment-detail")
    public String detail(ModelMap m, @RequestParam int id){
        m.put("assignmentAnswer", assignmentAnswerService.findById(id));
        return "teacher/TCH-AD006";
    }

    @PostMapping("assignment-check")
    public String check(@RequestParam int id, @RequestParam int mark){
        var assignmentAnswer = assignmentAnswerService.findById(id);
        assignmentAnswer.setMark(mark);
        assignmentAnswerService.save(assignmentAnswer);
        return "redirect:/teacher/batch-detail?batchId=%d".formatted(assignmentAnswer.getAssignment().getBatch().getId());
    }

    @GetMapping("exam-list")
    public String exams(ModelMap m){
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        var batches = user.getBatches();
        var examAnswers = new ArrayList<StudentHasExam>();
        for(var b: batches){
            for(var e: b.getBatchHasExams()){
                for(var s: e.getExam().getStudentHasExams()){
                    examAnswers.add(s);
                }
            }
        }
        
        m.put("exam", examAnswers);
        return "teacher/TCH-ET008";
    }

    @PostMapping("exam-schedule")
    public String examSchedule(@RequestParam int batchId, 
                                @RequestParam int examId,
                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime start,
                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime end,
                                RedirectAttributes attributes){

        if(start == null){
            if(end == null){

            }
            return "redirect:/teacher/batch-detail?batchId=%d#exam-tab".formatted(batchId);
        }

        if(end == null){
            return "redirect:/teacher/batch-detail?batchId=%d#exam-tab".formatted(batchId);
        }

        var batchHasExams = batchHasExamRepository.findByBatchId(batchId);
        var exam = examService.findById(examId);
        var scheduleExam = new BatchHasExam();
        for(var e : batchHasExams){
            if(e.getExam().getId() == examId){
                scheduleExam = e;
            }
        }
        scheduleExam.setEnd(end);
        scheduleExam.setStart(start);
        batchHasExamRepository.save(scheduleExam);
        attributes.addFlashAttribute("message", "%s scheduled successfully!".formatted(exam.getTitle()));
        return "redirect:/teacher/batch-detail?batchId=%d#exam-tab".formatted(batchId);
    }

    @ModelAttribute("assignment")
    public Assignment assignment(){
        return new Assignment();
    }

    @ModelAttribute("teacher")
    public void teacher(ModelMap m){
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        m.put("teacher", user);
    }
}
