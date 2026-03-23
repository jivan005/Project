package com.ailifeos.repository;

import com.ailifeos.entity.XPLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XPLogRepository extends JpaRepository<XPLog, Long> {
    List<XPLog> findByUserId(Long userId);
}
