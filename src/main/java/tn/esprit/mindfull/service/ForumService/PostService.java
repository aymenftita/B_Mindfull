package tn.esprit.mindfull.service.ForumService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Respository.ForumRepository.PostRepository;
import tn.esprit.mindfull.entity.forum.Post;
import tn.esprit.mindfull.user.User;
import tn.esprit.mindfull.user.UserRepository;
import tn.esprit.mindfull.user.UserService;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;


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

}
