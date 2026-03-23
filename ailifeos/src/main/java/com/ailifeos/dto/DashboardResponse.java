package com.ailifeos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {
    private String username;
    private int xp;
    private int level;
    private int streak;
    private List<TaskDto> pendingTasks;
    private List<String> weakAreas;
}
