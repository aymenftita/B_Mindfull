package tn.esprit.mindfull.Controller;


import tn.esprit.mindfull.entity.Message;
import tn.esprit.mindfull.Respository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class MessageWebSocketController {
    @Autowired
    private MessageRepository messagesRepository;

    @MessageMapping("/group/{groupId}")
    @SendTo("/topic/group/{groupId}")
    public Message sendGroupMessage(@DestinationVariable Long groupId, Message message) {
        message.setGroupId(groupId);
        message.setTimestamp(LocalDateTime.now());
        return messagesRepository.save(message);
    }

    @MessageMapping("/private")
    @SendTo("/topic/private/{recipientId}")
    public Message sendPrivateMessage(Message message) {
        message.setTimestamp(LocalDateTime.now());
        return messagesRepository.save(message);
    }
}