package com.ailifeos.service;

import com.ailifeos.dto.DashboardResponse;
import com.ailifeos.dto.TaskDto;
import com.ailifeos.entity.Task;
import com.ailifeos.entity.User;
import com.ailifeos.exception.ResourceNotFoundException;
import com.ailifeos.repository.TaskRepository;
import com.ailifeos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public DashboardResponse getDashboard(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Task> tasks = taskRepository.findByUserId(userId);
        List<TaskDto> taskDtos = tasks.stream()
                .filter(t -> "PENDING".equals(t.getStatus()))
                .map(t -> {
                    TaskDto dto = new TaskDto();
                    dto.setId(t.getId());
                    dto.setTitle(t.getTitle());
                    dto.setType(t.getType());
                    dto.setStatus(t.getStatus());
                    dto.setDate(t.getDate());
                    return dto;
                })
                .collect(Collectors.toList());

        List<String> weakAreas = List.of("Dynamic Programming", "Graphs"); // Mock data

        return new DashboardResponse(
                user.getUsername(),
                user.getXp(),
                user.getLevel(),
                user.getStreak(),
                taskDtos,
                weakAreas
        );
    }

    public List<User> getLeaderboard() {
        return userRepository.findTop10ByOrderByXpDesc();
    }
}
