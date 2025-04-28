package tn.esprit.mindfull.Controller.ForumController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Repository.ForumRepository.PostRepository;
import tn.esprit.mindfull.dto.Forumdto.PostStatsDTO;
import tn.esprit.mindfull.entity.forum.Post;
import tn.esprit.mindfull.Service.ForumService.PostService;
import tn.esprit.mindfull.entity.User.User;
import tn.esprit.mindfull.Service.UserService.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/forum/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostRepository postRepository;

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        User currentUser = userService.getCurrentUser();
        post.setAuthor(currentUser);
        return postService.savePost(post);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            Post selectedPost = post.get();
            // Instead of setting total replies and reactions, just fetch the values
            int totalReplies = selectedPost.getTotalReplies();
            int totalReactions = selectedPost.getTotalReactions();

            // Optionally, you can create a DTO or modify the Post object to include the counts
            // Set these values in the response
            return ResponseEntity.ok(selectedPost);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping
    public List<Post> getAllPosts(@RequestParam(required = false) String tag) {
        if (tag != null && !tag.isEmpty()) {
            return postService.getPostsByTag(tag);
        } else {
            return postService.getAllPosts();
        }
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody Post post) {
        post.setId(id);
        return postService.savePost(post);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

    @GetMapping("/tag/{tag}")
    public List<Post> getPostsByTag(@PathVariable String tag) {
        return postService.getPostsByTag(tag);
    }

    @GetMapping("/{id}/view")
    public Post viewPost(@PathVariable Long id) {
        return postService.incrementViewCount(id);
    }

    @GetMapping("/top-posts")
    public List<Post> getTopPosts() {
        return postService.getTopPosts(); // Fetch top posts logic
    }

    // In PostController.java
    @GetMapping("/stats/posts-per-day")
    public List<PostStatsDTO> getPostsPerDay() {
        return postService.getPostsPerDay();
    }

    @GetMapping("/stats/top-users")
    public List<PostStatsDTO> getTopActiveUsers() {
        return postService.getTopActiveUsers();
    }


}
