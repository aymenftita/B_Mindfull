package tn.esprit.mindfull.Respository.ForumRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.forum.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
