package com.jea.ai.predict.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jea.ai.predict.model.Prediction;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    // Custom database queries can be defined here if necessary
}
