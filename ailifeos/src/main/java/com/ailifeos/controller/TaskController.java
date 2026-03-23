package com.ailifeos.controller;

import com.ailifeos.dto.MessageResponse;
import com.ailifeos.dto.TaskDto;
import com.ailifeos.security.UserDetailsImpl;
import com.ailifeos.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDto> createTask(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.createTask(userDetails.getId(), taskDto));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<TaskDto>> getUserTasks(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getUserTasks(id));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<MessageResponse> completeTask(@PathVariable Long id) {
        taskService.completeTask(id);
        return ResponseEntity.ok(new MessageResponse("Task completed successfully!"));
    }
}
