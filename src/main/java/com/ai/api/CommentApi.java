package com.ai.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ai.entity.Comment;
import com.ai.service.CommentService;
import com.ai.service.UserService;
import com.ai.service.VideoService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class CommentApi {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private VideoService videoService;
    
    @PostMapping("student/comment")
    public Comment comment(@RequestBody Comment comment){
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        var video = videoService.findById(comment.getVideoId());
        comment.setUser(user);
        comment.setVideo(video);
        comment.setCreated(LocalDateTime.now());
        return commentService.save(comment);
    }

    @GetMapping("student/comments/{videoId}")
    public List<Comment> comments(@PathVariable("videoId") int videoId){
        return commentService.findByVideoId(videoId);
    }

    @GetMapping("student/final-comment")
    public Comment finalComment(){
        return commentService.findAllByOrderByIdDesc().stream().findFirst().get();
    }
}
