package tn.esprit.mindfull.Service.ForumService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Respository.ForumRepository.ReportRepository;
import tn.esprit.mindfull.entity.forum.Comment;
import tn.esprit.mindfull.entity.forum.Post;
import tn.esprit.mindfull.entity.forum.Report;
import tn.esprit.mindfull.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private NotificationService notificationService; // inject the NotificationService

    public Report reportPost(Post post, User user, String reason) {
        Report report = Report.builder()
                .post(post)
                .reportedBy(user)
                .reason(reason)
                .reportTime(LocalDateTime.now())
                .build();

        Report savedReport = reportRepository.save(report);

        // Send notification
        sendReportNotification(user.getName(), post.getTitle(), true);

        return savedReport;
    }

    public Report reportComment(Comment comment, User user, String reason) {
        Report report = Report.builder()
                .comment(comment)
                .reportedBy(user)
                .reason(reason)
                .reportTime(LocalDateTime.now())
                .build();

        Report savedReport = reportRepository.save(report);

        // Send notification
        sendReportNotification(user.getName(), null, false);

        return savedReport;
    }

    private void sendReportNotification(String userName, String postTitle, boolean isPost) {
        try {
            Map<String, Object> notification = new HashMap<>();
            notification.put("type", "report");
            notification.put("user", userName);
            notification.put("postTitle", postTitle); // can be null for comment
            notification.put("targetType", isPost ? "Post" : "Comment");

            ObjectMapper mapper = new ObjectMapper();
            String jsonNotification = mapper.writeValueAsString(notification);

            notificationService.sendNotification(jsonNotification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Report> getReportsByPost(Long postId) {
        return reportRepository.findByPostId(postId);
    }

    public List<Report> getReportsByComment(Long commentId) {
        return reportRepository.findByCommentId(commentId);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }
}
