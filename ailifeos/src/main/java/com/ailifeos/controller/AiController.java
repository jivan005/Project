package com.ailifeos.controller;

import com.ailifeos.dto.AiEvaluationRequest;
import com.ailifeos.dto.AiEvaluationResponse;
import com.ailifeos.service.AiEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {
    private final AiEvaluationService aiEvaluationService;

    @PostMapping("/evaluate")
    public ResponseEntity<AiEvaluationResponse> evaluateAnswer(@RequestBody AiEvaluationRequest request) {
        return ResponseEntity.ok(aiEvaluationService.evaluateAnswer(request.getAnswer()));
    }
}
