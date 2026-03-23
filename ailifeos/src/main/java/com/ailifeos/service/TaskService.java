package com.ailifeos.service;

import com.ailifeos.dto.TaskDto;
import com.ailifeos.entity.Task;
import com.ailifeos.entity.User;
import com.ailifeos.entity.XPLog;
import com.ailifeos.exception.ResourceNotFoundException;
import com.ailifeos.repository.TaskRepository;
import com.ailifeos.repository.UserRepository;
import com.ailifeos.repository.XPLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final XPLogRepository xpLogRepository;

    public List<TaskDto> getUserTasks(Long userId) {
        return taskRepository.findByUserId(userId).stream().map(t -> {
            TaskDto dto = new TaskDto();
            dto.setId(t.getId());
            dto.setTitle(t.getTitle());
            dto.setType(t.getType());
            dto.setStatus(t.getStatus());
            dto.setDate(t.getDate());
            return dto;
        }).collect(Collectors.toList());
    }

    public TaskDto createTask(Long userId, TaskDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Task task = Task.builder()
                .user(user)
                .title(request.getTitle())
                .type(request.getType())
                .status("PENDING")
                .date(request.getDate())
                .build();
        
        task = taskRepository.save(task);
        request.setId(task.getId());
        request.setStatus(task.getStatus());
        return request;
    }

    @Transactional
    public void completeTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!"COMPLETED".equals(task.getStatus())) {
            task.setStatus("COMPLETED");
            taskRepository.save(task);

            User user = task.getUser();
            int xpEarned = 15;
            user.setXp(user.getXp() + xpEarned);
            
            // update level
            int newLevel = calculateLevel(user.getXp());
            user.setLevel(newLevel);
            
            userRepository.save(user);

            XPLog log = XPLog.builder()
                    .user(user)
                    .action("COMPLETED_TASK_" + task.getId())
                    .xpGained(xpEarned)
                    .build();
            xpLogRepository.save(log);
        }
    }

    private int calculateLevel(int xp) {
        if (xp < 100) return 1;
        if (xp < 250) return 2;
        if (xp < 500) return 3;
        if (xp < 1000) return 4;
        return (xp / 500) + 3;
    }
}
