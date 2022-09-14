package com.ai.service;

import com.ai.entity.User;
import com.ai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    public void save(User user) {
        userRepo.save(user);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public User findByLoginId(String loginId) {
        return userRepo.findByLoginId(loginId);
    }

    public void deleteById(String studentId) {
        userRepo.deleteById(studentId);
    }

    public int getCount() {
        return (int) userRepo.count();
    }
}
