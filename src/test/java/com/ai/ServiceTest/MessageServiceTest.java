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

import com.ai.entity.Message;
import com.ai.entity.User;
import com.ai.repository.MessageRepository;
import com.ai.service.MessageService;

@SpringBootTest
public class MessageServiceTest {
    
    @Mock
    MessageRepository messageRepository;

    @InjectMocks
    MessageService messageService;

    public Message messageObj(){
        Message message = Message.builder()
        .id(1)
        .message("message")
        .created(LocalDateTime.now())
        .user(new User())
        .build();
        return message;
    }

    public List<Message> messageList(){
        List<Message> messageList = new ArrayList<Message>();

        Message message1 = Message.builder()
        .id(1)
        .message("message")
        .created(LocalDateTime.now())
        .user(new User())
        .build();

        Message message2 = Message.builder()
        .id(2)
        .message("Hi everyone!")
        .created(LocalDateTime.now())
        .user(new User())
        .build();

        messageList.add(message1);
        messageList.add(message2);
        return messageList;
    }

    @Test
    public void saveMessageTest(){
        Message message = Message.builder().build();
        messageService.save(message);
        verify(messageRepository, times(1)).save(message);
    }

    @Test
    public void findAllMessagesTest(){
        List<Message> messageList = messageList();
        Mockito.when(messageRepository.findAll()).thenReturn(messageList);
        List<Message> messages = messageService.findAll();
        assertEquals(2, messages.size());
        verify(messageRepository, times(1)).findAll();
    }

}
