package com.ai.ServiceTest;

import static org.assertj.core.api.Assertions.assertThat;
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
import com.ai.entity.Batch;
import com.ai.repository.AssignmentRepository;
import com.ai.service.AssignmentService;

@SpringBootTest
public class AssignmentServiceTest {
    
    @Mock
    AssignmentRepository assignmentRepository;

    @InjectMocks
    AssignmentService assignmentService;

    public Assignment assignmentObj(){
        Assignment assignment = Assignment.builder()
        .id(1)
        .title("Assignment Obj")
        .start(LocalDateTime.of(2022, 1, 13, 8, 56))
        .end(LocalDateTime.of(2022, 10, 13, 8, 56))
        .mark(100)
        .file("Assignment Object File")
        .batch(new Batch())
        .assignmentAnswers(new ArrayList<AssignmentAnswer>())
        .batchId(1)
        //.files(user.pdf)
        .build();
        return assignment;
    }

    public List<Assignment> assignmentList(){
        List<Assignment> assignmentList = new ArrayList<Assignment>();
        Assignment assignment1 = Assignment.builder()
        .id(1)
        .title("Assignment Obj1")
        .start(LocalDateTime.of(2022, 1, 13, 8, 56))
        .end(LocalDateTime.of(2022, 2, 13, 8, 56))
        .mark(100)
        .file("Assignment Object File")
        .batch(new Batch())
        .assignmentAnswers(new ArrayList<AssignmentAnswer>())
        .batchId(1)
        //.files(user.pdf)
        .build();

        Assignment assignment2 = Assignment.builder()
        .id(2)
        .title("Assignment Obj2")
        .start(LocalDateTime.of(2022, 1, 13, 8, 56))
        .end(LocalDateTime.of(2022, 2, 13, 8, 56))
        .mark(100)
        .file("Assignment Object File")
        .batch(new Batch())
        .assignmentAnswers(new ArrayList<AssignmentAnswer>())
        .batchId(1)
        //.files(user.pdf)
        .build();

        Assignment assignment3 = Assignment.builder()
        .id(3)
        .title("Assignment Obj3")
        .start(LocalDateTime.of(2022, 1, 13, 8, 56))
        .end(LocalDateTime.of(2022, 2, 13, 8, 56))
        .mark(100)
        .file("Assignment Object File")
        .batch(new Batch())
        .assignmentAnswers(new ArrayList<AssignmentAnswer>())
        .batchId(1)
        //.files(user.pdf)
        .build();
        assignmentList.add(assignment1);
        assignmentList.add(assignment2);
        assignmentList.add(assignment3);
        return assignmentList;
    }

    @Test
    public void saveTest(){
        Assignment assignment = assignmentObj();
        assignmentService.save(assignment);
        verify(assignmentRepository, times(1)).save(assignment);
    }

    @Test
    public void findAllTest(){
        List<Assignment> assignmentList = assignmentList();
        Mockito.when(assignmentRepository.findAll()).thenReturn(assignmentList);
        List<Assignment> assignments = assignmentService.findAll();
        assertEquals(3, assignments.size());
        verify(assignmentRepository, times(1)).findAll();
    }

    @Test
    public void countTest(){
        Assignment assignment = assignmentObj();
        int serviceCount = assignmentService.count();
        int repositoryCount = (int) assignmentRepository.count();
        assertThat(assignment.getStart().isBefore(LocalDateTime.now()));
        assertEquals(serviceCount, repositoryCount);
    }

    @Test
    public void findByIdTest(){
        Assignment assignment = assignmentObj();
        Mockito.when(assignmentRepository.findById(1)).thenReturn(Optional.of(assignment));
        Assignment getAssignment = assignmentService.findById(1);
        assertEquals(1, getAssignment.getId());
        assertEquals("Assignment Obj", getAssignment.getTitle());
        assertEquals(LocalDateTime.of(2022, 1, 13, 8, 56), getAssignment.getStart());
    }

    @Test
    public void deleteByIdTest(){
		assignmentService.deleteById(1);
		verify(assignmentRepository,times(1)).deleteById(1);
    }
    
}
