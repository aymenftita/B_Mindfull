package tn.esprit.mindfull.controller.ForumController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Respository.ForumRepository.PostRepository;
import tn.esprit.mindfull.dto.Forumdto.ReactionStatsDTO;
import tn.esprit.mindfull.entity.forum.Post;
import tn.esprit.mindfull.entity.forum.Reaction;
import tn.esprit.mindfull.entity.forum.ReactionType;
import tn.esprit.mindfull.Service.ForumService.ReactionService;
import tn.esprit.mindfull.user.User;
import tn.esprit.mindfull.user.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/forum")
public class ReactionController {

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Add a reaction to a specific post
     */
    @PostMapping("/posts/{postId}/reactions")
    public ResponseEntity<Reaction> addReactionToPost(
            @PathVariable Long postId,
            @RequestBody Reaction reaction,
            @RequestParam String username
    ) {
        // Fetch the post by ID
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Fetch the user by username
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Set the post and user on the reaction
        reaction.setPost(post);
        reaction.setUser(user);

        // Save and return the reaction
        Reaction savedReaction = reactionService.saveReaction(reaction);
        return new ResponseEntity<>(savedReaction, HttpStatus.CREATED);
    }

    /**
     * Get the current user's reaction for a specific post
     */
    @GetMapping("/posts/{postId}/reactions/user-reaction")
    public ResponseEntity<ReactionType> getUserReaction(
            @PathVariable Long postId,
            @RequestParam String username
    ) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("Looking for user: " + username);
        System.out.println("Looking for post with ID: " + postId);

        return reactionService.getReactionByUserAndPost(user, post)
                .map(reaction -> ResponseEntity.ok(reaction.getType()))
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/posts/{postId}/reactions")
    public ResponseEntity<List<Reaction>> getReactionsForPost(@PathVariable Long postId) {
        List<Reaction> reactions = reactionService.getReactionsByPost(postId);
        return ResponseEntity.ok(reactions);
    }


    @DeleteMapping("/posts/{postId}/reactions")
    public ResponseEntity<Void> removeReaction(
            @PathVariable Long postId,
            @RequestParam String username,
            @RequestParam String type
    ) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findByName(username).orElseThrow();

        reactionService.removeReaction(user, post, type); // Call the removeReaction method in service
        return ResponseEntity.noContent().build(); // Return a successful response without content
    }

    @GetMapping("/reactions/stats/distribution")
    public ResponseEntity<List<ReactionStatsDTO>> getReactionDistribution() {
        return ResponseEntity.ok(reactionService.getReactionDistribution());
    }

    @GetMapping("/reactions/stats/most-reacted")
    public ResponseEntity<List<ReactionStatsDTO>> getMostReactedPosts() {
        return ResponseEntity.ok(reactionService.getMostReactedPosts());
    }

}
