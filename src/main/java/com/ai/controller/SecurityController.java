package com.ai.controller;

import com.ai.entity.User.Role;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping("/")
    public String home(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null && auth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.Admin.name()))){
            return "redirect:/admin/home";
        }
        if(auth!=null && auth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.Student.name()))){
            return "redirect:/student/home";
        }
        if(auth!=null && auth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.Teacher.name()))){
            return "redirect:/teacher/home";
        }
        return "redirect:/login";
    }
}
