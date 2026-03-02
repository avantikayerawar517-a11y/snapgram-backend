package com.example.demo.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String username;
    private String email;
    private String password; // Nantar aapan he encrypt karu
    private String imageUrl;
    private String bio;
    private String imageId;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> saves = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> followers = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> following = new HashSet<>();

    // Yache Getters aani Setters khali add kar:
    public List<String> getSaves() { return saves; }
    public void setSaves(List<String> saves) { this.saves = saves; }

    public Set<String> getFollowers() { return followers; }
    public void setFollowers(Set<String> followers) { this.followers = followers; }
    public Set<String> getFollowing() { return following; }
    public void setFollowing(Set<String> following) { this.following = following; }

    // Getters and Setters (IntelliJ/VS Code madhe Right click -> Generate -> Getters and Setters karun sagle select kar kiva khali dilela code wapar)
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    //public String getImageUrl() { return imageUrl; }
    //public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}