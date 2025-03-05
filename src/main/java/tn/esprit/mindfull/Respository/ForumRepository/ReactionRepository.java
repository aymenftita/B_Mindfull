package tn.esprit.mindfull.Respository.ForumRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.forum.Reaction;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Integer> {


}
