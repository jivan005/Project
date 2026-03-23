package com.ailifeos.controller;

import com.ailifeos.dto.SubmissionRequest;
import com.ailifeos.entity.Submission;
import com.ailifeos.security.UserDetailsImpl;
import com.ailifeos.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/submissions")
@RequiredArgsConstructor
public class SubmissionController {
    private final SubmissionService submissionService;

    @PostMapping
    public ResponseEntity<Submission> submitProblem(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody SubmissionRequest request) {
        return ResponseEntity.ok(submissionService.submitProblem(userDetails.getId(), request));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Submission>> getUserSubmissions(@PathVariable Long id) {
        return ResponseEntity.ok(submissionService.getUserSubmissions(id));
    }
}
