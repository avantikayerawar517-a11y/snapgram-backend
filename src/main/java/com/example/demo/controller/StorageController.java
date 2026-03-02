package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/storage")
@CrossOrigin(origins = "http://localhost:5173") // React app la allow karnyasaathi
public class StorageController {

    // Ya navacha folder aapo-aap tayar hoil jyat sagle photos save hotil
    private static final String UPLOAD_DIR = "uploads/";

    public StorageController() {
        // Project chalu hotana check karel ki folder ahe ka, nsel tar navin banvel
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    // ==========================================
    // 1. File Upload API
    // ==========================================
    @PostMapping("/upload")
    public Map<String, String> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            // Ek unique nav tayar karu (don images clash hou naye mhanun UUID)
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            
            // Image actual hard disk var save kar
            Files.write(filePath, file.getBytes());

            // React la image cha ID aani direct disnari URL return kar
            response.put("imageId", fileName);
            response.put("imageUrl", "http://localhost:8080/api/storage/preview/" + fileName);
            
            return response;
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage());
        }
    }

    // ==========================================
    // 2. File Dakhvnyasathi API (Preview)
    // ==========================================
    @GetMapping("/preview/{fileName}")
    public ResponseEntity<Resource> getFilePreview(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                // Browser la sangto ki hi ek image ahe
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) 
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("File not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading file");
        }
    }
}