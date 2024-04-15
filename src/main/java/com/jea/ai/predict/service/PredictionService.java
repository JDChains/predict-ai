package com.jea.ai.predict.service;
import lombok.RequiredArgsConstructor;
import lombok.var;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.cloud.aiplatform.v1.EndpointName;
import com.google.cloud.aiplatform.v1.PredictRequest;
import com.google.cloud.aiplatform.v1.PredictResponse;
import com.google.cloud.aiplatform.v1.PredictionServiceClient;
import com.google.cloud.aiplatform.v1.schema.predict.prediction.ClassificationPredictionResult;

import com.google.protobuf.util.JsonFormat;
import com.jea.ai.predict.model.Prediction;
import com.jea.ai.predict.repo.PredictionRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class PredictionService {
    private final PredictionRepository predictionRepository;

    @Value("${vertexai.project-id}")
    private String projectId;

    @Value("${vertexai.endpoint-id}")
    private String endpointId;

    @Value("${vertexai.location}")
    private String location;

    public Prediction savePrediction(Prediction prediction) {
        String result = makePredictions(prediction.getInputFeatures());
        prediction.setPredictionResult(result);
        return predictionRepository.save(prediction);
    }

    private String makePrediction(String inputFeatures) {
        EndpointName endpointName = EndpointName.of(projectId, location, endpointId);
        try (PredictionServiceClient client = PredictionServiceClient.create()) {
            // Create a struct with input features
            com.google.protobuf.Struct.Builder structBuilder = com.google.protobuf.Struct.newBuilder();
            structBuilder.putFields("feature", com.google.protobuf.Value.newBuilder().setStringValue(inputFeatures).build());

            com.google.protobuf.Value instance = com.google.protobuf.Value.newBuilder().setStructValue(structBuilder.build()).build();

            // Create the PredictRequest with the constructed instance
            PredictRequest request = PredictRequest.newBuilder()
                .setEndpoint(endpointName.toString())
                .addAllInstances(Collections.singletonList(instance))
                .build();

            // Perform the prediction
            var predictResponse = client.predict(request);

            if (!predictResponse.getPredictionsList().isEmpty()) {
            	com.google.protobuf.Value prediction = predictResponse.getPredictionsList().get(0);
                // Assuming the prediction output is a string; adjust as necessary
                JsonFormat.Printer printer = JsonFormat.printer();
                return printer.print(prediction);  
            }
            return "No prediction result available";
        } catch (IOException e) {
            throw new RuntimeException("Failed to make prediction: " + e.getMessage(), e);
        }
    }
    private String makePredictions(String inputFeatures) {
        EndpointName endpointName = EndpointName.of(projectId, location, endpointId);
        try (PredictionServiceClient client = PredictionServiceClient.create()) {
            // Create a struct with input features
            com.google.protobuf.Struct.Builder structBuilder = com.google.protobuf.Struct.newBuilder();
            structBuilder.putFields("feature", com.google.protobuf.Value.newBuilder().setStringValue(inputFeatures).build());

            com.google.protobuf.Value instance = com.google.protobuf.Value.newBuilder().setStructValue(structBuilder.build()).build();

            // Create the PredictRequest with the constructed instance
            PredictRequest request = PredictRequest.newBuilder()
                .setEndpoint(endpointName.toString())
                .addAllInstances(Collections.singletonList(instance))
                .build();

            // Perform the prediction
            var predictResponse = client.predict(request);

            if (!predictResponse.getPredictionsList().isEmpty()) {
            	com.google.protobuf.Value prediction = predictResponse.getPredictionsList().get(0);
                // Assuming the prediction output is a string; adjust as necessary
                return prediction.toString(); // Simplify parsing for demonstration
            }
            return "No prediction result available";
        } catch (IOException e) {
            throw new RuntimeException("Failed to make prediction: " + e.getMessage(), e);
        }
    }
    public Prediction getPrediction(Long id) {
        // Using Java Optional to handle cases where prediction might not be found
        return predictionRepository.findById(id).orElseThrow(() -> new RuntimeException("Prediction not found with id: " + id));
    }

    public List<Prediction> getAllPredictions() {
        return predictionRepository.findAll();
    }
    @Transactional(readOnly = false)
    public void deletePrediction(Long id) {
        if (predictionRepository.existsById(id)) {
            predictionRepository.deleteById(id);
        } else {
            throw new RuntimeException("Prediction not found with id: " + id);
        }
    }
}