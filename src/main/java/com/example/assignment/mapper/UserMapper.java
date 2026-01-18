package com.example.assignment.mapper;

import org.springframework.stereotype.Component;

import com.example.assignment.model.User;
import com.example.assignment.model.UserResponse;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }
}
