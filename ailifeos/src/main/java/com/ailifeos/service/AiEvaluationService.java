package com.ailifeos.service;

import com.ailifeos.dto.AiEvaluationResponse;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AiEvaluationService {
    
    public AiEvaluationResponse evaluateAnswer(String answer) {
        // Mock implementation
        Random random = new Random();
        int score = random.nextInt(11); // 0 to 10
        String feedback;

        if (score >= 8) {
            feedback = "Excellent answer! You covered all the key points.";
        } else if (score >= 5) {
            feedback = "Good answer, but could be more detailed.";
        } else {
            feedback = "Needs improvement. Try to focus on the core concepts.";
        }

        return new AiEvaluationResponse(score, feedback);
    }
}
