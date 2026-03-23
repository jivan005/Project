package com.ailifeos.dto;

import lombok.Data;

@Data
public class SubmissionRequest {
    private Long problemId;
    private String codeSnippet;
}
