package tn.esprit.mindfull.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Respository.UserRepository;
import tn.esprit.mindfull.Service.UserService;
import tn.esprit.mindfull.configuration.JwtUtils;
import tn.esprit.mindfull.dto.LoginRequest;
import tn.esprit.mindfull.dto.UserRegistrationRequest;
import tn.esprit.mindfull.model.User;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

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
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = userService.loginUser(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(Map.of("token", token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            JwtUtils jwtUtils = new JwtUtils();
            String username = jwtUtils.extractUsername(token);
            Optional<tn.esprit.mindfull.model.User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setSessionToken(null);
                userRepository.save(user);
            }
        }
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
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
