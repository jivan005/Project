package com.ailifeos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "xp_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XPLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String action;

    private Integer xpGained;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
