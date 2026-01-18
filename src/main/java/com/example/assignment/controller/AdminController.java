package com.example.assignment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.assignment.model.ApiResponse;
import com.example.assignment.model.UserRequest;
import com.example.assignment.model.UserResponse;
import com.example.assignment.service.AdminService;

@RestController
@RequestMapping("/api/v1/admin")
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

    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserRole(
            @PathVariable Long id,
            @RequestBody UserRequest request) {

        UserResponse response = adminService.updateRole(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, response, null)
        );
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> deleteUser(
            @PathVariable Long id) {

        try {
            UserResponse deletedUser = adminService.deleteUser(id);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, deletedUser, null)
            );

        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, null, e.getMessage()));
        }
    }
}
