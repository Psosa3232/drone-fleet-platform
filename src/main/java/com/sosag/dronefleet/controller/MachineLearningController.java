package com.sosag.dronefleet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * REST controller for Machine Learning predictions.
 *
 * <p>Acts as a proxy to the Python ML microservice, providing
 * battery level predictions based on flight parameters.</p>
 */
@RestController
@RequestMapping("/api/v1/ml")
@Tag(name = "Machine Learning", description = "Battery prediction endpoints powered by ML models")
public class MachineLearningController {

    private final RestTemplate restTemplate;
    private final String mlServiceUrl = "http://localhost:5000/predict";

    public MachineLearningController() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Predicts battery level based on flight parameters.
     *
     * @param predictionRequest flight parameters (speed, altitude, temperature, distance)
     * @return predicted battery level
     */
    @PostMapping("/predict")
    @Operation(
            summary = "Predict battery level",
            description = "Uses a trained ML model to predict battery consumption based on flight parameters"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prediction successful"),
            @ApiResponse(responseCode = "500", description = "ML service unavailable or prediction failed")
    })
    public ResponseEntity<Map<String, Object>> predictBatteryLevel(
            @RequestBody Map<String, Double> predictionRequest) {
        
        try {
            // Call Python ML service
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(
                    mlServiceUrl, 
                    predictionRequest, 
                    Map.class
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of(
                            "error", "ML service unavailable: " + e.getMessage(),
                            "status", "error"
                    ));
        }
    }
}