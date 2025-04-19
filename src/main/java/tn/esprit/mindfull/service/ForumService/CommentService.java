package tn.esprit.mindfull.service.ForumService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Respository.ForumRepository.CommentRepository;
import tn.esprit.mindfull.entity.forum.Comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NotificationService notificationService;

    public Comment saveComment(Comment comment) {
        Comment saved = commentRepository.save(comment);

        // Creating structured notification message
        try {
            // Assuming "User1" as static user for now. Replace it with dynamic user data once ready.
            String username = "User1";
            String message = saved.getContent();

            // Create a map for structured data
            Map<String, String> notification = new HashMap<>();
            notification.put("type", "comment");
            notification.put("user", username);
            notification.put("message", message);

            // Convert map to JSON string
            ObjectMapper mapper = new ObjectMapper();
            String jsonNotification = mapper.writeValueAsString(notification);

            // Send notification as JSON
            notificationService.sendNotification(jsonNotification);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return saved;
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public long countCommentsByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }

}
