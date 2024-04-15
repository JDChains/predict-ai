package com.jea.ai.predict.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.jea.ai.predict.model.MedicalImage;
import com.jea.ai.predict.service.MedicalImageService;

@RestController
@RequestMapping("/api/medical-images")
public class MedicalImageController {
    @Autowired
    private MedicalImageService medicalImageService;

    @GetMapping("/analyze/{id}")
    public ResponseEntity<MedicalImage> analyzeImage(@PathVariable Long id) {
        MedicalImage image = medicalImageService.analyzeImage(id);
        return ResponseEntity.ok(image);
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            MedicalImage savedImage = medicalImageService.storeImage(file);
            return ResponseEntity.ok("Image stored successfully with ID: " + savedImage.getId());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to store image: " + e.getMessage());
        }
    }
}
