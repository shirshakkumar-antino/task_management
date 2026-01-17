package com.example.assignment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.assignment.model.ApiResponse;
import com.example.assignment.model.Task;
import com.example.assignment.service.TaskServices;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskServices taskServices;
    @PostMapping("/uploadTask")
    public ResponseEntity<ApiResponse<Task>> uploadTask(
            @RequestBody Task userTask) {

        try {
            Task task = taskServices.uploadTask(userTask);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, task, null)
            );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, null, "Task not created"));
        }
    }

    @GetMapping("/task")
    public ResponseEntity<ApiResponse<List<Task>>> getTask() {

        try {
            List<Task> allTask = taskServices.getTasksForCurrentUser();
            return ResponseEntity.ok(
                    new ApiResponse<>(true, allTask, null)
            );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, null, "Cannot fetch tasks"));
        }
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<ApiResponse<Task>> getTaskById(
            @PathVariable Long id) {

        try {
            Task task = taskServices.getTaskForCurrentUser(id);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, task, null)
            );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, null, "Task not found"));
        }
    }
}
