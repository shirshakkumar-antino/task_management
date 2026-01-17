package com.example.assignment.mapper;

import com.example.assignment.model.Task;
import com.example.assignment.model.TaskResponse;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponse toResponse(Task task) {
        TaskResponse res = new TaskResponse();
        res.setId(task.getId());
        res.setTitle(task.getTitle());
        res.setDescription(task.getDescription());
        res.setStatus(task.getStatus());
        res.setPriority(task.getPriority());
        res.setDueDate(task.getDueDate());
        return res;
    }
}
