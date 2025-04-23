package tn.esprit.mindfull.service.ForumService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Respository.ForumRepository.CommentRepository;
import tn.esprit.mindfull.entity.forum.Comment;
import tn.esprit.mindfull.entity.forum.Post;
import tn.esprit.mindfull.user.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    public Comment saveComment(Comment comment) {
        Comment saved = commentRepository.save(comment);

        try {
            String username = userService.getCurrentUser().getName();  // Get static user
            String postTitle = "Untitled";

            if (saved.getPost() != null && saved.getPost().getId() != null) {
                Post fullPost = postService.getPostById(saved.getPost().getId());
                if (fullPost != null && fullPost.getTitle() != null) {
                    postTitle = fullPost.getTitle();
                }
            }

            Map<String, String> notification = new HashMap<>();
            notification.put("type", "comment");
            notification.put("user", username);
            notification.put("message", saved.getContent());
            notification.put("postTitle", postTitle);

            ObjectMapper mapper = new ObjectMapper();
            String jsonNotification = mapper.writeValueAsString(notification);
            System.out.println("Sending notification: " + jsonNotification);
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
