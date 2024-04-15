package com.jea.ai.predict.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.Data;

@Entity
@Data
public class MedicalImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    private String imageData;  // Base64 encoded image data
    @Lob
    private byte[] medicalImageData;  // Binary data of the image
    private String analysisResult;
    private String status;  // e.g., "Pending", "Analyzed"
    
}
