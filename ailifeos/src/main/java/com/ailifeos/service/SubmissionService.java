package com.ailifeos.service;

import com.ailifeos.dto.AiEvaluationResponse;
import com.ailifeos.dto.SubmissionRequest;
import com.ailifeos.entity.Problem;
import com.ailifeos.entity.Submission;
import com.ailifeos.entity.User;
import com.ailifeos.entity.XPLog;
import com.ailifeos.exception.ResourceNotFoundException;
import com.ailifeos.repository.ProblemRepository;
import com.ailifeos.repository.SubmissionRepository;
import com.ailifeos.repository.UserRepository;
import com.ailifeos.repository.XPLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final XPLogRepository xpLogRepository;
    private final AiEvaluationService aiEvaluationService;

    @Transactional
    public Submission submitProblem(Long userId, SubmissionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found"));

        // Evaluate using AI (Mock)
        AiEvaluationResponse evaluation = aiEvaluationService.evaluateAnswer(request.getCodeSnippet());
        
        boolean isCorrect = evaluation.getScore() >= 5;
        String status = isCorrect ? "ACCEPTED" : "REJECTED";
        
        int xpEarned = 0;
        if (isCorrect) {
            xpEarned = 20; // Base XP for solve
            if (evaluation.getScore() >= 8) {
                xpEarned += 10; // Bonus
            }
        }

        Submission submission = Submission.builder()
                .user(user)
                .problem(problem)
                .codeSnippet(request.getCodeSnippet())
                .status(status)
                .xpEarned(xpEarned)
                .build();
        submissionRepository.save(submission);

        if (xpEarned > 0) {
            user.setXp(user.getXp() + xpEarned);
            updateUserLevel(user);
            userRepository.save(user);

            XPLog log = XPLog.builder()
                    .user(user)
                    .action("SOLVED_PROBLEM_" + problem.getId())
                    .xpGained(xpEarned)
                    .build();
            xpLogRepository.save(log);
        }

        return submission;
    }

    private void updateUserLevel(User user) {
        int newLevel = calculateLevel(user.getXp());
        user.setLevel(newLevel);
    }

    private int calculateLevel(int xp) {
        if (xp < 100) return 1;
        if (xp < 250) return 2;
        if (xp < 500) return 3;
        if (xp < 1000) return 4;
        return (xp / 500) + 3;
    }

    public List<Submission> getUserSubmissions(Long userId) {
        return submissionRepository.findByUserId(userId);
    }
}
