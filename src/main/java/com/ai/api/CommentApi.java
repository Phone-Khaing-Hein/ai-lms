package com.ai.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.entity.Comment;
import com.ai.service.CommentService;
import com.ai.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class CommentApi {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;
    
    @PostMapping("student/comment")
    public Comment comment(@RequestBody Comment comment){
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        comment.setUser(user);
        comment.setCreated(LocalDateTime.now());
        return commentService.save(comment);
    }

    @GetMapping("student/comments")
    public List<Comment> comments(){
        return commentService.findAll();
    }

    @GetMapping("student/final-comment")
    public Comment finalComment(){
        return commentService.findAllByOrderByIdDesc().stream().findFirst().get();
    }
}
