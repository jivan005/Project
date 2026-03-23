package com.ailifeos.service;

import com.ailifeos.entity.Problem;
import com.ailifeos.exception.ResourceNotFoundException;
import com.ailifeos.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;

    public List<Problem> getAllProblems() {
        return problemRepository.findAll();
    }

    public Problem getProblemById(Long id) {
        return problemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found with id: " + id));
    }

    public Problem createProblem(Problem problem) {
        return problemRepository.save(problem);
    }
}
