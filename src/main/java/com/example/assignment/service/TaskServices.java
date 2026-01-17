package com.example.assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

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

    public Task uploadTask(Task task) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        task.setUser(user);

        return taskRepository.save(task);
    }
}
