package com.ai.api;

import com.ai.entity.Message;
import com.ai.entity.User;
import com.ai.service.FileService;
import com.ai.service.MessageService;
import com.ai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class MessageApi {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private FileService fileService;

    @PostMapping("student/chat")
    public Message chat(@RequestBody Message message){
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        message.setUser(user);
        message.setCreated(LocalDateTime.now());
        var m = messageService.save(message);
        return m;
    }

    @GetMapping("student/chat/messages")
    public List<Message> messages(){
        return messageService.findAll();
    }

    @GetMapping("student/chat/user")
    public User user(){
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        return user;
    }

    @PostMapping("student/profile-change")
    @CrossOrigin
    public void profileChange(@RequestBody MultipartFile profile) throws IOException {
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        if(StringUtils.hasLength(user.getPhoto()) && !user.getPhoto().equals("default.png")){
            var profileFilePath = new File("src\\main\\resources\\static\\profile\\").getAbsolutePath();
            fileService.deleteFile(profileFilePath.concat("\\").concat(user.getPhoto()));
        }
        var fileName = fileService.createProfileFile(profile);
        user.setPhoto(fileName);
        userService.save(user);
    }
}
