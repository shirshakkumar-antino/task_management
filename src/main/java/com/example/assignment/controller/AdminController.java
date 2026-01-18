package com.example.assignment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.assignment.model.ApiResponse;
import com.example.assignment.model.UserResponse;
import com.example.assignment.service.AdminService;

@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {

        List<UserResponse> users = adminService.getAllUsers();

        return ResponseEntity.ok(
                new ApiResponse<>(true, users, null)
        );
    }

}
