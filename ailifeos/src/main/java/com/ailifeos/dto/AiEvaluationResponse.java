package com.ailifeos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiEvaluationResponse {
    private int score;
    private String feedback;
}
