package com.ai.api;

import com.ai.entity.Message;
import com.ai.entity.User;
import com.ai.service.BatchService;
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
    private BatchService batchService;

    @PostMapping({"student/chat", "teacher/chat"})
    public Message chat(@RequestBody Message message){
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        message.setUser(user);
        message.setCreated(LocalDateTime.now());
        message.setBatch(batchService.findById(message.getBatchId()));
        var m = messageService.save(message);
        return m;
    }

    @GetMapping({"student/chat/messages/{id}","teacher/chat/messages/{id}"})
    public List<Message> messages(@PathVariable int id){
        return messageService.findAll().stream().filter(m -> m.getBatch().getId() == id).toList();
    }

    @GetMapping({"student/chat/user","teacher/chat/user"})
    public User user(){
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        return user;
    }
}
