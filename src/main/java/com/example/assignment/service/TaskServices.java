package com.example.assignment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.assignment.model.Task;
import com.example.assignment.model.User;
import com.example.assignment.repository.TaskRepository;
import com.example.assignment.repository.UserRepository;

@Service
public class TaskServices {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // CREATE TASK
    public Task uploadTask(Task task) {

        User user = getCurrentUser();
        task.setUser(user);

        return taskRepository.save(task);
    }

    // GET ALL TASKS FOR LOGGED-IN USER
    public List<Task> getTasksForCurrentUser() {

        User user = getCurrentUser();
        return taskRepository.findByUserId(user.getId());
    }

    // GET SINGLE TASK BY ID (SECURE)
    public Task getTaskForCurrentUser(Long taskId) {

        User user = getCurrentUser();

        return taskRepository
                .findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Task not found or access denied"));
    }
    private User getCurrentUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
