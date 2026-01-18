package com.example.assignment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import com.example.assignment.mapper.UserMapper;
import com.example.assignment.model.User;
import com.example.assignment.model.UserResponse;
import com.example.assignment.repository.UserRepository;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

     public List<UserResponse> getAllUsers() {

        User currentUser = getCurrentUser();

        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new RuntimeException("Unauthorized access");
        }

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
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
