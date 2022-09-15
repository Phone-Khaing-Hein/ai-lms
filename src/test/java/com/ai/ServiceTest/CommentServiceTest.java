package com.ai.ServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.entity.Comment;
import com.ai.entity.Module;
import com.ai.entity.User;
import com.ai.entity.Video;
import com.ai.entity.User.Role;
import com.ai.repository.CommentRepository;
import com.ai.service.CommentService;

@SpringBootTest
public class CommentServiceTest {
    
    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommentService commentService;

    public User userObj(){
        User user = User.builder()
        .loginId("ADM001")
        .name("Admin User")
        .password("admin")
        .role(Role.Admin)
        .email("phyuthin2004@gmail.com")
        .isActive(true)
        .build();
        return user;
    }

    public Video videoObj(){
        Video video = Video.builder()
        .id(1)
        .name("Video")
        .build();
        return video;
    }

//    public List<Video> videoList(){
//        Video video
//    }

    // public Module moduleObj(){
    //     Module module = Module.builder().
    //     .
    //     build();
    // }

    public Comment commentObj(){
        Comment comment = Comment.builder()
        .id(1)
        .comment("Thanks")
        .created(LocalDateTime.now())
        .video(videoObj())
        .user(userObj())
        .videoId(1)
        .build();
        return comment;
    }

    public List<Comment> commentList(){
        List<Comment> commentList = new ArrayList<Comment>();
        Comment comment1 = Comment.builder()
        .id(1)
        .comment("Thanks")
        .created(LocalDateTime.now())
        .video(videoObj())
        .user(userObj())
        .videoId(1)
        .build();

        Comment comment2 = Comment.builder()
        .id(2)
        .comment("Thank you")
        .created(LocalDateTime.now())
        .video(videoObj())
        .user(userObj())
        .videoId(2)
        .build();

        commentList.add(comment1);
        commentList.add(comment2);
        return commentList;
    }

    @Test
    public void saveCommentTest(){
        Comment comment = commentObj();
        commentService.save(comment);
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    public void findAllCommentTest(){
        List<Comment> commentList = commentList();
        Mockito.when(commentRepository.findAll()).thenReturn(commentList);
        List<Comment> comments = commentService.findAll();
        assertEquals(2, comments.size());
        verify(commentRepository, times(1)).findAll();
    }

    @Test
    public void findAllByOrderByIdDescTest(){
        List<Comment> commentList = commentList();
        Mockito.when(commentRepository.findAllByOrderByIdDesc()).thenReturn(commentList);
        List<Comment> comments = commentRepository.findAllByOrderByIdDesc();
        assertEquals(2, comments.size());
        verify(commentRepository, times(1)).findAllByOrderByIdDesc();
    }

    @Test
    public void deleteByIdTest(){
        commentService.delete(1);
		verify(commentRepository,times(1)).deleteById(1);
    }

    @Test
    public void findByVideoIdTest(){
        List<Comment> commentList = commentList();
        Mockito.when(commentRepository.findByVideo_Id(2)).thenReturn(commentList);
        List<Comment> comments = commentService.findByVideoId(2);
        assertEquals(commentList.size(), comments.size());
    }

}
