package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Post;
import com.example.demo.repository.Postrepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/posts")
//@CrossOrigin(origins = "http://localhost:5173") // React la allow karnyasaathi
public class PostController {

    @Autowired
    private Postrepository postRepository;

    // Post banvnyachi API
    @PostMapping("/create")
    public Post createPost(@RequestBody Post post) {
        return postRepository.save(post);
    }

    // Home page var sagle posts dakhvnyachi API
    @GetMapping("/recent")
    public List<Post> getRecentPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }
    // Post la Like / Unlike karnyachi API
    @PutMapping("/like/{postId}")
    public Post likePost(@PathVariable Long postId, @RequestBody List<String> likesArray) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        // Frontend kadhun aaleli navin likes chi list post madhe taku
        post.setLikes(likesArray);
        return postRepository.save(post);
    }
    // User chya ID varun posts ghenyachi API
    @GetMapping("/user/{userId}")
    public List<Post> getUserPosts(@PathVariable Long userId) {
        return postRepository.findByCreatorIdOrderByCreatedAtDesc(userId);
    }

    // ==========================================
    // 4. Get Post by ID API (Edit karnyacha aadhi post dakhvnyasathi)
    // ==========================================
    @GetMapping("/{postId}")
    public Post getPostById(@PathVariable Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    // ==========================================
    // 5. Delete Post API
    // ==========================================
    @DeleteMapping("/delete/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postRepository.deleteById(postId);
    }

    // ==========================================
    // 6. Update Post API
    // ==========================================
    @PutMapping("/update/{postId}")
    public Post updatePost(@PathVariable Long postId, @RequestBody Post updatedPost) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        post.setCaption(updatedPost.getCaption());
        post.setLocation(updatedPost.getLocation());
        post.setTags(updatedPost.getTags());
        
        // Jar navin image aali asel tar update kar
        if (updatedPost.getImageUrl() != null && !updatedPost.getImageUrl().isEmpty()) {
            post.setImageUrl(updatedPost.getImageUrl());
            post.setImageId(updatedPost.getImageId());
        }
        
        return postRepository.save(post);
    }
    // ==========================================
    // 7. Search Posts API
    // ==========================================
    @GetMapping("/search/{searchTerm}")
    public List<Post> searchPosts(@PathVariable String searchTerm) {
        return postRepository.findByCaptionContainingIgnoreCase(searchTerm);
    }
    // ==========================================
    // 8. Get Liked Posts API
    // ==========================================
    @GetMapping("/liked/{userId}")
    public List<Post> getLikedPosts(@PathVariable String userId) {
        return postRepository.findByLikesContaining(userId);
    }
}
