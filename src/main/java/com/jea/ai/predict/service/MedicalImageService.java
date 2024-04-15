package com.jea.ai.predict.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.aiplatform.v1.EndpointName;
import com.google.cloud.aiplatform.v1.PredictionServiceClient;
import com.google.protobuf.ByteString;

import com.google.cloud.aiplatform.v1.PredictRequest;
import com.google.protobuf.util.JsonFormat;
import com.jea.ai.predict.model.MedicalImage;
import com.jea.ai.predict.repo.MedicalImageRepository;

import lombok.var;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;

@Service
public class MedicalImageService {
    @Autowired
    private MedicalImageRepository repository;

    @Value("${vertexai.project-id}")
    private String projectId;

    @Value("${vertexai.endpoint-id}")
    private String endpointId;

    @Value("${vertexai.location}")
    private String location;

    public MedicalImage analyzeImage(Long id) {
        MedicalImage image = repository.findById(id).orElseThrow(() -> new RuntimeException("Image not found"));
        if ("Pending".equals(image.getStatus())) {
            String result = sendToVertexAI(image.getImageData());
            image.setAnalysisResult(result);
            image.setStatus("Analyzed");
            repository.save(image);
        }
        return image;
    }

    private String sendToVertexAI(String base64ImageData) {
        ByteString imageBytes = ByteString.copyFrom(Base64.getDecoder().decode(base64ImageData));
        com.google.protobuf.Value imageValue = com.google.protobuf.Value.newBuilder().setStringValue(base64ImageData).build();

        EndpointName endpointName = EndpointName.of(projectId, location, endpointId);
        PredictRequest predictRequest = PredictRequest.newBuilder()
                .setEndpoint(endpointName.toString())
                .addAllInstances(Collections.singletonList(com.google.protobuf.Value.newBuilder().setStringValue(imageValue.getStringValue()).build()))
                .build();

        try (PredictionServiceClient client = PredictionServiceClient.create()) {
            var predictResponse = client.predict(predictRequest);
            return JsonFormat.printer().print(predictResponse);
        } catch (Exception e) {
            throw new RuntimeException("Failed to predict image", e);
        }
    }
    public MedicalImage storeImage(MultipartFile file) throws IOException {
        MedicalImage image = new MedicalImage();
        image.setMedicalImageData(file.getBytes());
        image.setStatus("Pending"); // Initial status
        return repository.save(image);
    }
}
