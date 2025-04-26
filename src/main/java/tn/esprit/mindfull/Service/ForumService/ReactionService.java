package tn.esprit.mindfull.Service.ForumService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Repository.ForumRepository.PostRepository;
import tn.esprit.mindfull.Repository.ForumRepository.ReactionRepository;
import tn.esprit.mindfull.dto.Forumdto.ReactionStatsDTO;
import tn.esprit.mindfull.entity.forum.Post;
import tn.esprit.mindfull.entity.forum.Reaction;
import tn.esprit.mindfull.entity.forum.ReactionType;
import tn.esprit.mindfull.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReactionService {
    @Autowired
    private ReactionRepository reactionRepository;
    @Autowired
    private PostRepository postRepository;


    public Reaction saveReaction(Reaction reaction) {
        Optional<Reaction> existingReaction = reactionRepository.findByPostAndUser(reaction.getPost(), reaction.getUser());

        if (existingReaction.isPresent()) {
            Reaction toUpdate = existingReaction.get();
            toUpdate.setType(reaction.getType());
            toUpdate.setCreationTime(LocalDateTime.now());
            return reactionRepository.save(toUpdate);
        } else {
            return reactionRepository.save(reaction);
        }
    }

    public List<Reaction> getReactionsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return reactionRepository.findByPost(post);
    }


    public Optional<Reaction> getReactionByUserAndPost(User user, Post post) {
        return reactionRepository.findByPostAndUser(post, user);
    }

    public Map<ReactionType, Long> countReactionsByType(Long postId) {
        List<Reaction> reactions = reactionRepository.findByPostId(postId);
        return reactions.stream()
                .collect(Collectors.groupingBy(Reaction::getType, Collectors.counting()));
    }

    public void removeReaction(User user, Post post, String reactionType) {
        Optional<Reaction> optionalReaction = Optional.ofNullable(reactionRepository.findByUserAndPost(user, post));
        optionalReaction.ifPresent(reaction -> {
            if (reaction.getType().toString().equals(reactionType)) {
                reactionRepository.delete(reaction);
            }
        });
    }

    public List<ReactionStatsDTO> getReactionDistribution() {
        return reactionRepository.countReactionsByType().stream()
                .map(obj -> new ReactionStatsDTO(
                        ((ReactionType) obj[0]).name(),
                        ((Number) obj[1]).intValue(),
                        false // Not a post
                ))
                .collect(Collectors.toList());
    }

    public List<ReactionStatsDTO> getMostReactedPosts() {
        return reactionRepository.findMostReactedPosts().stream()
                .map(obj -> new ReactionStatsDTO(
                        (String) obj[0],
                        ((Number) obj[1]).intValue(),
                        true // Is a post
                ))
                .collect(Collectors.toList());
    }
}
