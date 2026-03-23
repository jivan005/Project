package com.ailifeos.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDto {
    private Long id;
    private String title;
    private String type;
    private String status;
    private LocalDate date;
}
