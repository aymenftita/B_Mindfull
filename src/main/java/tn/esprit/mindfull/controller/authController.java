package tn.esprit.mindfull.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.mindfull.Respository.UserRepository;
import tn.esprit.mindfull.Service.UserService;
import tn.esprit.mindfull.dto.LoginRequest;
import tn.esprit.mindfull.dto.UserRegistrationRequest;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class authController {
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Validated @RequestBody UserRegistrationRequest request
    ) {
        try {
            String token = userService.registerUser(request);
            return ResponseEntity.ok(Map.of("token", token)); // Return JSON object
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = userService.loginUser(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(token);
    }
    // authController.java


    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            userService.initiatePasswordReset(request.get("email"));
            return ResponseEntity.ok()
                    .body(Collections.singletonMap("message", "Password reset instructions sent to email"));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            userService.resetPassword(
                    request.get("token"),
                    request.get("newPassword")
            );
            return ResponseEntity.ok().body(Map.of("message", "Password has been reset successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
