package com.ai.controller;

import com.ai.entity.Attendance;
import com.ai.entity.Module;
import com.ai.entity.Schedule;
import com.ai.entity.User;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
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
        m.put("schedule",new Schedule());
        return "teacher/TCH-BD003";
    }

    @GetMapping("createAttendance")
    public String createAttendance(@RequestParam int batchId,
                                   ModelMap m) {
        var batch = batchService.findById(batchId);
        m.put("batch", batch);
        m.put("students", batch.getUsers().stream().filter(u -> u.getRole().equals(User.Role.Student)).toList());
        return "teacher/TCH-AT001";
    }
    
    @PostMapping("setAttendance")
    public String setAttendance(
            @RequestParam int batchId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam List<String> status,
            @RequestParam List<String> loginId,
            ModelMap m) {
        //Attendance attend = attendanceRepository.findByDate(date);

        // if (attend.getDate().equals(date)) {
        //     System.out.println("Date already existed !! =>" + date);
        //     m.put("error", "Attendance for " + date + " already created!!");
        //     return "TCH-AT001";
        // }
        var batch = batchService.findById(batchId);

        for (var i = 0; i < status.size(); i++) {
            for (var j = i; j < loginId.size(); ) {
                var attendance = new Attendance();
                attendance.setAttendanceStatus(status.get(i));
                attendance.setBatch(batch);
                attendance.setDate(date);
                attendance.setUser(userService.findByLoginId(loginId.get(j)));
                attendanceService.save(attendance);
                break;
            }
        }
        return "redirect:/teacher/batch-detail?batchId=" + batchId + "#attendance-tab";
    }


    @PostMapping("save-schedule")
    public String saveSchedule(@RequestParam int batchId, @RequestParam int moduleId, @ModelAttribute @Valid Schedule schedule, BindingResult bs){

        if(bs.hasErrors()){

        }
        else {
            schedule.setScheduleDate(schedule.getScheduleDate());
            Module module = moduleService.findById(moduleId);
            schedule.setModule(module);
            schedule.setBatch(batchService.findById(batchId));
            scheduleRepository.save(schedule);
        }
        return "redirect:/teacher/batch-detail?batchId="+batchId;
    }



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
        return "student/STU-CH004";
    }
}
