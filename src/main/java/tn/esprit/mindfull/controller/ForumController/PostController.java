package tn.esprit.mindfull.controller.ForumController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.entity.forum.Post;
import tn.esprit.mindfull.service.ForumService.PostService;
import tn.esprit.mindfull.user.User;
import tn.esprit.mindfull.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/forum/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        User currentUser = userService.getCurrentUser();
        post.setAuthor(currentUser);
        return postService.savePost(post);
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
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

}
