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

import com.ai.entity.Batch;
import com.ai.entity.BatchHasExam;
import com.ai.entity.Exam;
import com.ai.repository.BatchHasExamRepository;
import com.ai.service.BatchHasExamService;

@SpringBootTest
public class BatchHasExamServiceTest {
    
    @Mock
    BatchHasExamRepository batchHasExamRepository;

    @InjectMocks
    BatchHasExamService batchHasExamService;
    
    public BatchHasExam batchHasExamObj(){
        BatchHasExam bhe = BatchHasExam.builder()
        .id(1)
        .start(LocalDateTime.now())
        .end(LocalDateTime.now())
        .batch(new Batch())
        .exam(new Exam())
        .build();
        return bhe;
    }

    public List<BatchHasExam> batchHasExams(){
        List<BatchHasExam> bheList = new ArrayList<BatchHasExam>();

        BatchHasExam bhe1 = BatchHasExam.builder()
        .id(1)
        .start(LocalDateTime.now())
        .end(LocalDateTime.now())
        .batch(new Batch())
        .exam(new Exam())
        .build();

        BatchHasExam bhe2 = BatchHasExam.builder()
        .id(2)
        .start(LocalDateTime.now())
        .end(LocalDateTime.now())
        .batch(new Batch())
        .exam(new Exam())
        .build();

        bheList.add(bhe1);
        bheList.add(bhe2);

        return bheList;
    }

    @Test
    public void getBatchHasExamListByBatchIdTest(){
        List<BatchHasExam> bheList = batchHasExams();
        Mockito.when(batchHasExamRepository.findByBatchId(1)).thenReturn(bheList);
        List<BatchHasExam> bhes = batchHasExamService.getBatchHasExamListByBatchId(1);
        verify(batchHasExamRepository,times(1)).findByBatchId(1);
        assertEquals(bheList.get(1).getId(), bhes.get(1).getId());
        }

    @Test
    public void getBatchHasExamByIdTest(){
        BatchHasExam bhe = batchHasExamObj();
        Mockito.when(batchHasExamRepository.findById(1)).thenReturn(Optional.of(bhe));
        BatchHasExam getBhe = batchHasExamService.getBatchHasExamById(1);
        verify(batchHasExamRepository,times(1)).findById(1);
        assertEquals(1, getBhe.getId());
    }
}
