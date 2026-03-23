package com.ailifeos.dto;

import lombok.Data;

@Data
public class AiEvaluationRequest {
    private Long submissionId;
    private String answer;
}
