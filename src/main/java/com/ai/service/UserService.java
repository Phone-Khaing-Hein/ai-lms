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

//    public List<User> findByUserId(int batchId) {
//        return userRepo.findByBatchId(batchId).stream().filter(a -> a.getRole().equals(User.Role.Teacher)).toList();
//    }
    public User findByLoginId(String loginId) {
    return userRepo.findById(loginId).get();
}



        public List<User> findUserByTeacherRole(){
            return userRepo.findAll().stream().filter(a -> a.getRole().equals(User.Role.Teacher)).toList();
    }

}
