package com.example.assignment.controller;

import com.example.assignment.config.JwtService;
import com.example.assignment.model.ApiResponse;
import com.example.assignment.model.LoginRequest;
import com.example.assignment.model.LoginResponse;
import com.example.assignment.model.RefreshToken;
import com.example.assignment.model.RefreshTokenRequest;
import com.example.assignment.model.RefreshTokenResponse;
import com.example.assignment.model.User;
import com.example.assignment.repository.UserRepository;
import com.example.assignment.service.RefreshTokenService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                                        user.getEmail(),
                        user.getPassword(),
                        List.of(() -> "ROLE_" + user.getRole())
                )
        );

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        return ResponseEntity.ok(
                new LoginResponse(
                        accessToken,
                        refreshToken.getToken(),
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole()
                )
        );
    }

@PostMapping("/register")
public ResponseEntity<ApiResponse<String>> register(@RequestBody User user) {

    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, null, "Email already exists"));
    }

    String password = user.getPassword();

    if (password == null || password.length() < 8) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, null, "Password must be at least 8 characters long"));
    }

    if (!password.matches(".*[A-Z].*")) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, null, "Password must contain at least one uppercase letter"));
    }

    if (!password.matches(".*[a-z].*")) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, null, "Password must contain at least one lowercase letter"));
    }

    if (!password.matches(".*\\d.*")) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, null, "Password must contain at least one number"));
    }

    user.setPassword(passwordEncoder.encode(password));

    userRepository.save(user);

    return ResponseEntity.ok(
            new ApiResponse<>(true, "User registered successfully", null)
    );
}

    @PostMapping("/refresh")
        public ResponseEntity<RefreshTokenResponse> refreshToken(
                @RequestBody RefreshTokenRequest request) {

        RefreshToken refreshToken =
                refreshTokenService
                        .getByToken(request.getRefreshToken())
                        .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        refreshTokenService.verifyExpiration(refreshToken);

        User user = refreshToken.getUser();

        String newAccessToken = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        List.of(() -> "ROLE_" + user.getRole())
                )
        );

        return ResponseEntity.ok(
                new RefreshTokenResponse(
                        newAccessToken,
                        refreshToken.getToken()
                )
        );
        }

        @PostMapping("/logout")
        public ResponseEntity<ApiResponse<String>> logout(
                @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, null, "Invalid token"));
        }

        String accessToken = authHeader.substring(7);
        String email = jwtService.extractUsername(accessToken);

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        refreshTokenService.deleteByUser(user);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Logged out successfully", null)
        );
        }


}
