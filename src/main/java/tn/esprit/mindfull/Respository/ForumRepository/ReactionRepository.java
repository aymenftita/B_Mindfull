package tn.esprit.mindfull.Respository.ForumRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.forum.Post;
import tn.esprit.mindfull.entity.forum.Reaction;
import tn.esprit.mindfull.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findByPostAndUser(Post post, User user);
    List<Reaction> findByPostId(Long postId);
    Reaction findByUserAndPost(User user, Post post);
    List<Reaction> findByPost(Post post);
    @Query("SELECT r.type AS reactionType, COUNT(r) AS count FROM Reaction r GROUP BY r.type")
    List<Object[]> countReactionsByType();

    @Query("SELECT p.title AS postTitle, COUNT(r) AS count " +
            "FROM Reaction r " +
            "JOIN r.post p " +
            "GROUP BY p.title " +
            "ORDER BY count DESC")
    List<Object[]> findMostReactedPosts();
}
