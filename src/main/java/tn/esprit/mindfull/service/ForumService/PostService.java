package tn.esprit.mindfull.service.ForumService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Respository.ForumRepository.CommentRepository;
import tn.esprit.mindfull.Respository.ForumRepository.PostRepository;
import tn.esprit.mindfull.entity.forum.Post;
import tn.esprit.mindfull.user.User;
import tn.esprit.mindfull.user.UserRepository;
import tn.esprit.mindfull.user.UserService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentRepository commentRepository;

    public Post savePost(Post post) {
        User staticAuthor = userService.getCurrentUser();
        post.setAuthor(staticAuthor);
        return postRepository.save(post);
    }


    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public List<Post> getPostsByTag(String tag) {
        return postRepository.findByTag(tag);
    }

    public Post incrementViewCount(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setViewCount(post.getViewCount() + 1);
        return postRepository.save(post);
    }




    public List<Post> getTopPosts() {
        List<Post> posts = postRepository.findAll();

        // Sort the posts based on the number of comments
        return posts.stream()
                .sorted((post1, post2) -> Long.compare(commentRepository.countByPostId(post2.getId()), commentRepository.countByPostId(post1.getId())))
                .limit(5) // Limit to top 5 posts
                .collect(Collectors.toList());
    }

}
