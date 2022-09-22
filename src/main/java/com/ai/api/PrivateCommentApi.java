package com.ai.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.entity.PrivateComment;
import com.ai.service.AssignmentAnswerService;
import com.ai.service.AssignmentService;
import com.ai.service.PrivateCommentService;
import com.ai.service.UserService;

@RestController
public class PrivateCommentApi {

    @Autowired
    private UserService userService;

    @Autowired
    private AssignmentAnswerService assignmentAnswerService;

    @Autowired
    private PrivateCommentService privateCommentService;

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping("teacher/private-comment")
    public PrivateComment privateComment(@RequestBody PrivateComment pc){
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        var assignment = assignmentAnswerService.findById(pc.getAssignmentId());
        var privateComment = new PrivateComment();
        privateComment.setUser(user);
        privateComment.setAssignmentAnswer(assignment);
        privateComment.setComment(pc.getComment());
        return privateCommentService.save(privateComment);
    }

    @PostMapping("student/private-comment")
    public PrivateComment privateCommentStudent(@RequestBody PrivateComment pc){
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        var assignment = assignmentService.findById(pc.getAssignmentId());
        var privateComment = new PrivateComment();
        privateComment.setUser(user);
        privateComment.setAssignmentAnswer(assignment.getAssignmentAnswers().stream().filter(a -> a.getUser().getLoginId().equals(loginId)).toList().get(0));
        privateComment.setComment(pc.getComment());
        return privateCommentService.save(privateComment);
    }

    @GetMapping("teacher/private-messages")
    public List<PrivateComment> privateComments(@RequestParam int assignmentId){
        System.out.println(assignmentId);
        var comments = privateCommentService.findByAssignmentId(assignmentId);
        System.out.println(comments.size());
        return comments;
    }

    @GetMapping("student/private-messages")
    public List<PrivateComment> privateCommentsForStudent(@RequestParam int assignmentId){
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var assignment = assignmentService.findById(assignmentId);
        List<PrivateComment> comments = new ArrayList<>();
        for(var a : assignment.getAssignmentAnswers()){
            if(a.getUser().getLoginId().equals(loginId)){
                comments = privateCommentService.findByAssignmentId(a.getId());
            }
        }
        return comments;
    }
    
}
