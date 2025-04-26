package tn.esprit.mindfull.Repository.ForumRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.dto.Forumdto.PostStatsDTO;
import tn.esprit.mindfull.entity.forum.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTag(String tag);

    // repository/PostRepository.java
    @Query("SELECT FUNCTION('DATE', p.creationTime) AS date, COUNT(p) FROM Post p GROUP BY FUNCTION('DATE', p.creationTime)")
    List<Object[]> countPostsGroupedByDate();

    @Query("SELECT new tn.esprit.mindfull.dto.Forumdto.PostStatsDTO(p.author.firstName, COUNT(p)) " +
            "FROM Post p GROUP BY p.author.firstName ORDER BY COUNT(p) DESC")
    List<PostStatsDTO> countPostsByUser();
}
