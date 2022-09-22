package com.ai.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.entity.PrivateComment;
import com.ai.service.AssignmentService;
import com.ai.service.PrivateCommentService;
import com.ai.service.UserService;

@RestController
public class PrivateCommentApi {

    @Autowired
    private UserService userService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private PrivateCommentService privateCommentService;

    @PostMapping("teacher/private-comment")
    public PrivateComment privateComment(@RequestBody PrivateComment pc){
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        var assignment = assignmentService.findById(pc.getAssignmentId());
        var privateComment = new PrivateComment();
        privateComment.setUser(user);
        privateComment.setAssignment(assignment);
        privateComment.setComment(pc.getComment());
        return privateCommentService.save(privateComment);
    }

    @GetMapping("teacher/private-messages")
    public List<PrivateComment> privateComments(@RequestParam int assignmentId){
        var comments = privateCommentService.findByAssignmentId(assignmentId);
        System.out.println(comments.size());
        return comments;
    }
    
}
