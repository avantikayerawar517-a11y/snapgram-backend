package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

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

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173") // Tujhya React app (Snapgram) la allow karnyasaathi
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // ==========================================
    // 1. User Registration API (Signup)
    // ==========================================
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        // Database madhe navin user save karto
        return userRepository.save(user);
    }

    // ==========================================
    // 2. User Login API
    // ==========================================
    @PostMapping("/login")
    public User loginUser(@RequestBody User loginRequest) {
        Optional<User> existingUser = userRepository.findByEmail(loginRequest.getEmail());

        // Password match jhala tar purna User chi JSON object return karel
        if (existingUser.isPresent() && existingUser.get().getPassword().equals(loginRequest.getPassword())) {
            return existingUser.get(); 
        } else {
            // Match nahi jhala tar error deil
            throw new RuntimeException("Invalid Email or Password");
        }
    }
    

    // ==========================================
    // 3. Save Post API
    // ==========================================
    @PostMapping("/save/{userId}/{postId}")
    public User saveUserPost(@PathVariable Long userId, @PathVariable String postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Jar list madhe aadhi pasun post nsel, tar add kar
        if(!user.getSaves().contains(postId)) {
            user.getSaves().add(postId);
        }
        return userRepository.save(user); // Updated user return kar
    }

    // ==========================================
    // 4. Unsave Post API
    // ==========================================
    @DeleteMapping("/save/{userId}/{postId}")
    public User removeSavedPost(@PathVariable Long userId, @PathVariable String postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.getSaves().remove(postId); // List madhun post kadhun tak
        return userRepository.save(user); // Updated user return kar
    }
    // 5. User chya ID varun chi mahiti ghenyachi API
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // 6. User chi Profile Edit karnyachi API
    @PutMapping("/update")
    public User updateUser(@RequestBody User updatedUser) {
        User user = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setName(updatedUser.getName());
        user.setBio(updatedUser.getBio());
        
        // Jar navin photo aala asel tar update kar
        if (updatedUser.getImageUrl() != null && !updatedUser.getImageUrl().isEmpty()) {
            user.setImageUrl(updatedUser.getImageUrl());
            user.setImageId(updatedUser.getImageId());
        }
        return userRepository.save(user);
    }
    // ==========================================
    // 7. Search Users API (People page sathi)
    // ==========================================
    @GetMapping("/search/{searchTerm}")
    public List<User> searchUsers(@PathVariable String searchTerm) {
        return userRepository.findByNameContainingIgnoreCase(searchTerm);
    }
    // ==========================================
    // 8. Follow / Unfollow User API
    // ==========================================
    @PutMapping("/follow/{currentUserId}/{targetUserId}")
    public User toggleFollowUser(@PathVariable Long currentUserId, @PathVariable Long targetUserId) {
        if(currentUserId.equals(targetUserId)) {
            throw new RuntimeException("You cannot follow yourself");
        }

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Current User not found"));
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target User not found"));

        String targetIdStr = String.valueOf(targetUserId);
        String currentIdStr = String.valueOf(currentUserId);

        // Logic: Jar aadhi pasun following madhe asel, tar kadhun tak (Unfollow)
        if (currentUser.getFollowing().contains(targetIdStr)) {
            currentUser.getFollowing().remove(targetIdStr);
            targetUser.getFollowers().remove(currentIdStr);
        } else {
            // Nahi tar add kar (Follow)
            currentUser.getFollowing().add(targetIdStr);
            targetUser.getFollowers().add(currentIdStr);
        }

        // Donhi users database madhe save kar
        userRepository.save(targetUser);
        return userRepository.save(currentUser);
    }
    // ==========================================
    // 9. Get All Users API (People page sathi)
    // ==========================================
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
}

