package tn.esprit.mindfull.controller.ForumController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.dto.Forumdto.CommentStatsDTO;
import tn.esprit.mindfull.dto.Forumdto.PostCommentStatsDTO;
import tn.esprit.mindfull.entity.forum.Comment;
import tn.esprit.mindfull.entity.forum.Post;
import tn.esprit.mindfull.Service.ForumService.CommentService;

import java.util.List;

@RestController
@RequestMapping("/forum/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping
    public Comment createComment(@RequestBody Comment comment, @RequestParam Long postId) {
        Post post = new Post();
        post.setId(postId);
        comment.setPost(post);
        return commentService.saveComment(comment);
    }

    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    @GetMapping
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }

    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        Comment existingComment = commentService.getCommentById(id);
        if (existingComment != null) {
            existingComment.setContent(comment.getContent());
            return commentService.saveComment(existingComment); // Save the updated comment
        }
        return null;
    }


    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }

    @GetMapping("/byPost/{postId}")
    public List<Comment> getCommentsByPost(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/count/{postId}")
    public long getCommentCountByPost(@PathVariable Long postId) {
        return commentService.countCommentsByPostId(postId);
    }
    @GetMapping("/stats")
    public ResponseEntity<List<CommentStatsDTO>> getCommentStatistics() {
        return ResponseEntity.ok(commentService.getCommentStatsOverTime());
    }

    @GetMapping("/stats/popular-posts")
    public ResponseEntity<List<PostCommentStatsDTO>> getPopularPosts() {
        return ResponseEntity.ok(commentService.getMostCommentedPosts());
    }
}