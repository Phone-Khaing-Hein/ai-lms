package com.ai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.entity.Answer;
import com.ai.repository.AnswerRepository;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    public Answer save(Answer answer) {
        return answerRepository.save(answer);
    }
    
}
