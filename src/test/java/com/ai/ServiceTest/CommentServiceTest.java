package com.ai.ServiceTest;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.entity.Comment;
import com.ai.repository.CommentRepository;
import com.ai.service.CommentService;

@SpringBootTest
public class CommentServiceTest {
    
    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommentService commentService;

    // public User userObj(){

    // }

    // public Video videoObj(){

    // }
    @Test
    public void saveCommentTest(){
        Comment comment = Comment.builder()
        .id(1)
        .comment("Thanks")
        .created(LocalDateTime.now())
        // .video(video)
        // .user(user)
        .build();
    }
}
