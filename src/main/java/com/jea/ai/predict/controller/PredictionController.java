package com.jea.ai.predict.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jea.ai.predict.PredictionDTO;
import com.jea.ai.predict.model.Prediction;
import com.jea.ai.predict.service.PredictionService;

@RestController
@RequestMapping("/api/predictions")
@RequiredArgsConstructor
public class PredictionController {
    private final PredictionService predictionService;

    @PostMapping
    public ResponseEntity<Prediction> createPrediction(@RequestBody PredictionDTO predictionDTO) {
        Prediction prediction = new Prediction();
        prediction.setInputFeatures(predictionDTO.getInputFeatures());
        Prediction savedPrediction = predictionService.savePrediction(prediction);
        return ResponseEntity.ok(savedPrediction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prediction> getPrediction(@PathVariable Long id) {
        Prediction prediction = predictionService.getPrediction(id);
        return ResponseEntity.ok(prediction);
    }

    @GetMapping
    public ResponseEntity<Iterable<Prediction>> getAllPredictions() {
        return ResponseEntity.ok(predictionService.getAllPredictions());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrediction(@PathVariable Long id) {
        predictionService.deletePrediction(id);
        return ResponseEntity.ok().build();
    }
    
}
