package tn.esprit.mindfull.Service.ForumService;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Repository.ForumRepository.CommentRepository;
import tn.esprit.mindfull.Repository.ForumRepository.PostRepository;
import tn.esprit.mindfull.Repository.ForumRepository.ReportRepository;
import tn.esprit.mindfull.Repository.UserRepository.UserRepository;
import tn.esprit.mindfull.dto.Forumdto.PostStatsDTO;
import tn.esprit.mindfull.entity.forum.Comment;
import tn.esprit.mindfull.entity.forum.Post;
import tn.esprit.mindfull.entity.User.User;
import tn.esprit.mindfull.Service.UserService.UserService;

import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private ReportRepository reportRepository;

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
/*
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }*/
public List<Post> getPostsByTag(String tag) {
    return postRepository.findByTag(tag);
}


    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Delete all reports related to comments of this post
        for (Comment comment : post.getComments()) {
            reportRepository.deleteByComment(comment);
        }

        // Delete reports linked directly to post
        reportRepository.deleteByPost(post);

        // Delete all comments (safe now that reports are gone)
        commentRepository.deleteAll(post.getComments());

        // Finally, delete the post
        postRepository.delete(post);
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

    public List<PostStatsDTO> getPostsPerDay() {
        List<Object[]> results = postRepository.countPostsGroupedByDate();
        List<PostStatsDTO> stats = results.stream()
                .map(obj -> new PostStatsDTO(
                        obj[0].toString(),  // date as String
                        ((Number) obj[1]).longValue()  // count as long
                ))
                .collect(Collectors.toList());

        // Log output to verify
        stats.forEach(stat -> System.out.println(stat.getLabel() + ": " + stat.getValue()));

        return stats;
    }



    public List<PostStatsDTO> getTopActiveUsers() {
        return postRepository.countPostsByUser();
    }

}
