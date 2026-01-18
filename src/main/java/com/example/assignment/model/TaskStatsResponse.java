package com.example.assignment.model;

import java.util.Map;
import lombok.Data;

@Data
public class TaskStatsResponse {

    private long totalTasks;
    private long completedTasks;
    private long pendingTasks;

    // priority -> count (HIGH, MEDIUM, LOW)
    private Map<String, Long> tasksByPriority;
}
