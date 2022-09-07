package com.ai.service;

import com.ai.entity.Message;
import com.ai.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    public Message save(Message messageEntity) {
        return messageRepository.save(messageEntity);
    }
}
