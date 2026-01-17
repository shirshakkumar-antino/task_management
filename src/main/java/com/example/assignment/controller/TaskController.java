package com.example.assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.assignment.model.ApiResponse;
import com.example.assignment.model.Task;
import com.example.assignment.service.TaskServices;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskServices taskServices;
    @PostMapping("/uploadTask")
    public ResponseEntity<ApiResponse<Task>> uploadTask(@RequestBody Task userTask){
        try{
            Task task= taskServices.uploadTask(userTask);
            return ResponseEntity.ok(new ApiResponse<>(true,task,null));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false,null,"Task not created"));
        }
    }
}
