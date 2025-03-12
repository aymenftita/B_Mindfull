package tn.esprit.mindfull.service.ForumService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Respository.ForumRepository.ReactionRepository;
import tn.esprit.mindfull.entity.forum.Reaction;

@Service
public class ReactionService {
    @Autowired
    private ReactionRepository reactionRepository;
    public Reaction saveReaction(Reaction reaction) { return reactionRepository.save(reaction); }
}
