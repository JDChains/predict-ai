package com.jea.ai.predict.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jea.ai.predict.model.MedicalImage;

@Repository
public interface MedicalImageRepository extends JpaRepository<MedicalImage, Long> {
}
