package com.ai.ServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.entity.Assignment;
import com.ai.entity.AssignmentAnswer;
import com.ai.entity.User;
import com.ai.repository.AssignmentAnswerRepository;
import com.ai.service.AssignmentAnswerService;

@SpringBootTest
public class AssignmentAnswerServiceTest {
    
    @Mock
    AssignmentAnswerRepository assignmentAnswerRepository;

    @InjectMocks
    AssignmentAnswerService assignmentAnswerService;

    public AssignmentAnswer assignmentAnswerObj(){
        AssignmentAnswer assAns = AssignmentAnswer.builder()
        .id(1)
        .answer("answer")
        .user(new User())
        .created(LocalDateTime.now())
        .assignment(new Assignment())
        .mark(100)
        .build();
        return assAns;
    }

    public List<AssignmentAnswer> assignmentAnswers(){
        List<AssignmentAnswer> assAnsList = new ArrayList<AssignmentAnswer>();
        AssignmentAnswer assAns1 = assignmentAnswerObj();
        AssignmentAnswer assAns2 = AssignmentAnswer.builder()
        .id(2)
        .answer("answers")
        .user(new User())
        .created(LocalDateTime.now())
        .assignment(new Assignment())
        .mark(100)
        .build();
        assAnsList.add(assAns1);
        assAnsList.add(assAns2);
        return assAnsList;
    }

    @Test
    public void saveTest(){
        AssignmentAnswer assAns = assignmentAnswerObj();
        assignmentAnswerService.save(assAns);
        verify(assignmentAnswerRepository, times(1)).save(assAns);
    }

    @Test
    public void findAll(){
        List<AssignmentAnswer> assAnsList = assignmentAnswers();
        Mockito.when(assignmentAnswerRepository.findAll()).thenReturn(assAnsList);
        List<AssignmentAnswer> answers = assignmentAnswerService.findAll();
        assertEquals(2, answers.size());
        verify(assignmentAnswerRepository, times(1)).findAll();
    }

    @Test
    public void findByIdTest(){
        AssignmentAnswer assAns = assignmentAnswerObj();
        Mockito.when(assignmentAnswerRepository.findById(1)).thenReturn(Optional.of(assAns));
        AssignmentAnswer getAnswers = assignmentAnswerService.findById(1);
        assertEquals("answer", getAnswers.getAnswer());
        assertEquals(100, getAnswers.getMark());
    }
}
