package com.example.assignment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.assignment.model.ApiResponse;
import com.example.assignment.model.TaskCreateRequest;
import com.example.assignment.model.TaskResponse;
import com.example.assignment.model.TaskStatsResponse;
import com.example.assignment.model.TaskUpdateRequest;
import com.example.assignment.service.TaskServices;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    @Autowired
    private TaskServices taskServices;

    @PostMapping("/uploadTask")
    public ResponseEntity<ApiResponse<TaskResponse>> uploadTask(
            @RequestBody TaskCreateRequest userTask) {

        try {
            TaskResponse task = taskServices.uploadTask(userTask);
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
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getTask() {

        try {
            List<TaskResponse> allTask =
                    taskServices.getTasksForCurrentUser();

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
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(
            @PathVariable Long id) {

        try {
            TaskResponse task =
                    taskServices.getTaskForCurrentUser(id);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, task, null)
            );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, null, "Task not found"));
        }
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @PathVariable Long id,
            @RequestBody TaskUpdateRequest request) {

        try {
            TaskResponse updatedTask =
                    taskServices.updateTask(id, request);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, updatedTask, null)
            );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, null, e.getMessage()));
        }
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTask(@PathVariable Long id) {

        taskServices.deleteTask(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Task deleted successfully", null)
        );
    }
    @GetMapping("/task/stats")
    public ResponseEntity<ApiResponse<TaskStatsResponse>> getTaskStats() {

        TaskStatsResponse stats = taskServices.getTaskStats();

        return ResponseEntity.ok(
                new ApiResponse<>(true, stats, null)
        );
    }

}
