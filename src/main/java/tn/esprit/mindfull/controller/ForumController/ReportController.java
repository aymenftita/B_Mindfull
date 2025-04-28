package tn.esprit.mindfull.controller.ForumController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.entity.forum.Comment;
import tn.esprit.mindfull.entity.forum.Post;
import tn.esprit.mindfull.entity.forum.Report;
import tn.esprit.mindfull.Service.ForumService.CommentService;
import tn.esprit.mindfull.Service.ForumService.PostService;
import tn.esprit.mindfull.Service.ForumService.ReportService;
import tn.esprit.mindfull.entity.User.User;
import tn.esprit.mindfull.Service.UserService.UserService;

import java.util.List;

@RestController
@RequestMapping("/forum/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @PostMapping("/post/{postId}")
    public Report reportPost(@PathVariable Long postId, @RequestParam String reason) {
        User user = userService.getCurrentUser(); // Replace with your real user retrieval logic
        Post post = postService.getPostById(postId);
        return reportService.reportPost(post, user, reason);
    }

    @PostMapping("/comment/{commentId}")
    public Report reportComment(@PathVariable Long commentId, @RequestParam String reason) {
        User user = userService.getCurrentUser();
        Comment comment = commentService.getCommentById(commentId);
        return reportService.reportComment(comment, user, reason);
    }

    @GetMapping
    public List<Report> getAllReports() {
        return reportService.getAllReports();
    }

    @GetMapping("/post/{postId}")
    public List<Report> getReportsByPost(@PathVariable Long postId) {
        return reportService.getReportsByPost(postId);
    }

    @GetMapping("/comment/{commentId}")
    public List<Report> getReportsByComment(@PathVariable Long commentId) {
        return reportService.getReportsByComment(commentId);
    }
}
