package tn.esprit.mindfull.Services;

import tn.esprit.mindfull.entity.User;
import tn.esprit.mindfull.entity.Message;
import tn.esprit.mindfull.Respository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;


    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }


    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    public List<Message> getMessagesByUserId(Long userId) {
        return messageRepository.findByUserId(userId);
    }

    public Message updateMessage(Long id, Message messageDetails) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        message.setContent(messageDetails.getContent());
        message.setUser(messageDetails.getUser());

        return messageRepository.save(message);
    }

    public void deleteMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        messageRepository.delete(message);
    }
}