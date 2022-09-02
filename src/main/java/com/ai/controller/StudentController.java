package com.ai.controller;

import com.ai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("student")
public class StudentController {

    @Autowired
    private UserService userService;

    @GetMapping("home")
    public String home(){
        var auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "student/STU-DB001";
    }
}
