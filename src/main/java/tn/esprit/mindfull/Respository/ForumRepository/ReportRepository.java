package tn.esprit.mindfull.Respository.ForumRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.forum.Comment;
import tn.esprit.mindfull.entity.forum.Post;
import tn.esprit.mindfull.entity.forum.Report;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByPostId(Long postId);
    List<Report> findByCommentId(Long commentId);
    void deleteByPost(Post post);
    void deleteByPostId(Long postId);
    void deleteByComment(Comment comment);
}
