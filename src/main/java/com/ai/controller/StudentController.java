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
}
