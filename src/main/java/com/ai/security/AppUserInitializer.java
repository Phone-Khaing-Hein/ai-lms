package com.ai.security;

import com.ai.entity.User;
import com.ai.entity.User.Role;
import com.ai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class AppUserInitializer {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository repo;

    @Transactional
    @EventListener(classes = ContextRefreshedEvent.class)
    public void initializeUser() {
        if(repo.count() == 0) {
            var admin = new User();
            admin.setLoginId("ADM001");
            admin.setName("Admin User");
            admin.setActive(true);
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(Role.Admin);
            repo.save(admin);

            var teacher = new User();
            teacher.setLoginId("TCH001");
            teacher.setName("May Zwei");
            teacher.setActive(true);
            teacher.setEmail("mayzwei@gmail.com");
            teacher.setPassword(passwordEncoder.encode("teacher"));
            teacher.setRole(Role.Teacher);
            repo.save(teacher);

            var student = new User();
            student.setLoginId("STU001");
            student.setName("Phone Khaing Hein");
            student.setActive(true);
            student.setEmail("pkh2662003@gmail.com");
            student.setPassword(passwordEncoder.encode("student"));
            student.setRole(Role.Teacher);
            repo.save(student);
        }
    }
}
