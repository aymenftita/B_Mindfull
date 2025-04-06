package tn.esprit.mindfull.entity;

import tn.esprit.mindfull.entity.User;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;
    private Long groupId;
    private Long recipientId;
    private String content;
    private LocalDateTime timestamp;

    // Optionally add the user field if needed
    @ManyToOne
    private User user;
}