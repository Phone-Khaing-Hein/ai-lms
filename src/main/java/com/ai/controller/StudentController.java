package com.ai.controller;

import com.ai.entity.User;
import com.ai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("student")
public class StudentController {

    @Autowired
    private UserService userService;

    @GetMapping("home")
    public String home(ModelMap m){
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        m.put("user", user);
        return "student/STU-DB001";
    }

    @GetMapping("course")
    public String course(){
        return "student/STU-VD002";
    }

    @GetMapping("members")
    public String members(){
        return "student/STU-MB003";
    }

    @GetMapping("chat")
    public String chat(){
        return "student/STU-CH004";
    }

    @GetMapping("profile")
    public String profile(){
        return "student/STU-PF005";
    }
}
