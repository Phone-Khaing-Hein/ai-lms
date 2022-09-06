package com.ai.controller;

import com.ai.entity.User;
import com.ai.service.BatchService;
import com.ai.service.ModuleService;
import com.ai.service.UserService;
import com.ai.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("student")
public class StudentController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BatchService batchService;

    @GetMapping("home")
    public String home(ModelMap m){
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        var teachers = userService.findAll().stream()
                .filter(u -> u.getRole().equals(User.Role.Teacher))
                .filter(t -> t.getBatches().contains(batchService.findById(user.getBatches().get(0).getId()))).toList();
        m.put("user", user);
        m.put("teachers", teachers);
        return "student/STU-DB001";
    }

    @GetMapping("course")
    public String course(ModelMap m){
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        var modules = moduleService.findByCourseId(user.getBatches().get(0).getCourse().getId());
        m.put("modules", modules);
        return "student/STU-VD002";
    }

    @GetMapping("members")
    public String members(ModelMap m){
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        var members = batchService.findById(user.getBatches().get(0).getId()).getUsers();
        m.put("members", members.stream()
                .filter(s -> s.getRole().equals(User.Role.Student))
                .filter(u -> !u.getLoginId().equals(user.getLoginId())).toList());
        return "student/STU-MB003";
    }

    @GetMapping("chat")
    public String chat(){
        return "student/STU-CH004";
    }

    @GetMapping("profile")
    public String profile(ModelMap m){
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        m.put("user", user);
        return "student/STU-PF005";
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
            return "student/STU-PF005";
        }
        if(!StringUtils.hasLength(newPassword)){
            m.put("newPasswordError", "New Password is required!");
            return "student/STU-PF005";
        }
        if(oldPassword.equals(newPassword)){
            m.put("newPasswordError", "Old Password and New Password are same!");
            return "student/STU-PF005";
        }

        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            m.put("oldPasswordError", "Old Password is incorrect!");
            return "student/STU-PF005";
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.save(user);
        attributes.addFlashAttribute("message","Changed Password successfully!");
        return "redirect:/student/profile";
    }
}
