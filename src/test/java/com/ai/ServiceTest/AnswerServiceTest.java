package com.ai.ServiceTest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.entity.Answer;
import com.ai.entity.Question;
import com.ai.repository.AnswerRepository;
import com.ai.service.AnswerService;

@SpringBootTest
public class AnswerServiceTest {
    
    @Mock
    AnswerRepository answerRepository;

    @InjectMocks
    AnswerService answerService;

    public Answer answerObj(){
        Answer answer = Answer.builder()
        .id(1)
        .answer("answer")
        .question(new Question())
        .build();
        return answer;
    }
    
    @Test
    public void saveTest(){
        Answer answer = answerObj();
        answerService.save(answer);
        verify(answerRepository, times(1)).save(answer);
    }

}
