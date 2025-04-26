package tn.esprit.mindfull.Repository.ForumRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.forum.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    long countByPostId(Long postId);
    @Query("SELECT DATE_FORMAT(c.creationTime, '%Y-%m-%d') AS date, COUNT(c) FROM Comment c GROUP BY DATE_FORMAT(c.creationTime, '%Y-%m-%d')")
    List<Object[]> countCommentsByCreationDate();
    @Query("SELECT p.title AS postTitle, COUNT(c) AS commentCount " +
            "FROM Comment c JOIN c.post p GROUP BY p.title ORDER BY commentCount DESC")
    List<Object[]> findMostCommentedPosts();
}
