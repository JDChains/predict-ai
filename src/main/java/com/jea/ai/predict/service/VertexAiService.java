package com.jea.ai.predict.service;

import com.google.cloud.aiplatform.v1beta1.EndpointName;
import com.google.cloud.aiplatform.v1beta1.PredictionServiceClient;
import com.google.cloud.aiplatform.v1beta1.PredictRequest;
import com.google.cloud.aiplatform.v1beta1.PredictResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class VertexAiService {

    @Value("${vertexai.project-id}")
    private String projectId;

    @Value("${vertexai.endpoint-id}")
    private String endpointId;

    @Value("${vertexai.location}")
    private String location;
    
    public PredictResponse predict(List<com.google.protobuf.Value> instances) throws IOException {
        EndpointName endpointName = EndpointName.of(projectId, location, endpointId);
        PredictRequest predictRequest = PredictRequest.newBuilder()
                .setEndpoint(endpointName.toString())
                .addAllInstances(instances)
                .build();
        try (PredictionServiceClient client = PredictionServiceClient.create()) {
            return client.predict(predictRequest);
        }
    }


}
