package tn.esprit.mindfull.service.ForumService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Respository.ForumRepository.ReportRepository;
import tn.esprit.mindfull.entity.forum.Comment;
import tn.esprit.mindfull.entity.forum.Post;
import tn.esprit.mindfull.entity.forum.Report;
import tn.esprit.mindfull.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public Report reportPost(Post post, User user, String reason) {
        Report report = Report.builder()
                .post(post)
                .reportedBy(user)
                .reason(reason)
                .reportTime(LocalDateTime.now())
                .build();
        return reportRepository.save(report);
    }

    public Report reportComment(Comment comment, User user, String reason) {
        Report report = Report.builder()
                .comment(comment)
                .reportedBy(user)
                .reason(reason)
                .reportTime(LocalDateTime.now())
                .build();
        return reportRepository.save(report);
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
