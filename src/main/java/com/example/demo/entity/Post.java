package com.example.demo.entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caption;
    private String location;
    private String tags; // Aapan tags comma-separated string mhanun save karu (e.g. "nature,travel")
    private String imageUrl;
    private String imageId;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> likes = new ArrayList<>();
    
    // Yache Getters aani Setters pan khali add kar:
    public List<String> getLikes() { return likes; }
    public void setLikes(List<String> likes) { this.likes = likes; }

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // User sobat relationship (Ek user kitihi posts banvu shakto)
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    // Getters and Setters (IntelliJ/VS Code madhe generate karun ghe kiva khali dilele wapar)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getImageId() { return imageId; }
    public void setImageId(String imageId) { this.imageId = imageId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getCreator() { return creator; }
    public void setCreator(User creator) { this.creator = creator; }
}