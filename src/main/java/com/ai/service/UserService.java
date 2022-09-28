package com.ai.service;

import com.ai.entity.User;
import com.ai.repository.UserRepository;
import com.ai.util.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
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

    public void updateResetPasswordToken(String token,String email) throws UserNotFoundException{
        User user = userRepo.findByEmail(email);
        if (user!= null){
            user.setResetPasswordToken(token);
            userRepo.save(user);
        }
        else {
            throw new UserNotFoundException("Could not find any user with email\n"+email);
        }
    }

    public User get(String resetPasswordToken){
        return userRepo.findByResetPasswordToken(resetPasswordToken);
    }

    public void updatePassword(User user,String newPassword){
        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(newPassword);

        user.setPassword(encodePassword);
        user.setResetPasswordToken(null);
        userRepo.save(user);
    }
}
