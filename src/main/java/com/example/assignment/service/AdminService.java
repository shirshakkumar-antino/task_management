package com.example.assignment.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.assignment.mapper.UserMapper;
import com.example.assignment.model.User;
import com.example.assignment.model.UserRequest;
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

    public UserResponse updateRole(Long userId, UserRequest request) {

        User currentUser = getCurrentUser();

        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new RuntimeException("Only ADMIN can update user roles");
        }

        String newRole = request.getRole().toUpperCase();
        if (!newRole.equals("USER") && !newRole.equals("ADMIN")) {
            throw new IllegalArgumentException("Role must be USER or ADMIN");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(newRole);
        userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }

    public UserResponse deleteUser(Long userId) {

        User currentUser = getCurrentUser();
        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new RuntimeException("Only ADMIN can delete users");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getId().equals(currentUser.getId())) {
            throw new RuntimeException("Admin cannot delete self");
        }

        userRepository.delete(user);

        UserResponse response = new UserResponse();
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
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
