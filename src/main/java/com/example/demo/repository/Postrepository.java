package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Post;

@Repository
public interface Postrepository extends JpaRepository<Post, Long> {
    // Navin posts aadhi dakhvnyasaathi aapan ek custom query banvu:
    List<Post> findAllByOrderByCreatedAtDesc();
    // User chya ID varun tyache sagle posts aannari method
    List<Post> findByCreatorIdOrderByCreatedAtDesc(Long creatorId);
    List<Post> findByCaptionContainingIgnoreCase(String searchTerm);
    List<Post> findByLikesContaining(String userId);
}

