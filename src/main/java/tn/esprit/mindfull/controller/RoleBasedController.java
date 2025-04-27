package tn.esprit.mindfull.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Respository.UserRepository;
import tn.esprit.mindfull.Service.UserService;
import tn.esprit.mindfull.dto.UserUpdateRequest;
import tn.esprit.mindfull.model.AppRole;
import tn.esprit.mindfull.model.User;

import java.net.ContentHandler;
import java.nio.file.AccessDeniedException;
import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class RoleBasedController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/shared_D_A/getAllUsers")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<User>> getAllUsers() throws AccessDeniedException {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @DeleteMapping("/shared_D_A/deleteUserById/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            String message = userService.deleteUser(userId);
            return ResponseEntity.ok(message); // Return success message with 200 OK
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage()); // 403 Forbidden
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404 Not Found
        }
    }

    @GetMapping("/shared_All/getUserByUsername/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        try {
            UserDetails userDetails = userService.loadUserByUsername(username);
            return ResponseEntity.ok(Map.of(
                    "username", userDetails.getUsername(),
                    "email", ((User) userDetails).getEmail(),
                    "role", userDetails.getAuthorities()
            ));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/shared_All/getUserById/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            Optional<User> userOptional = userService.loadUserById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                Map<String, Object> response = new HashMap<>();

                response.put("id", user.getId());
                response.put("firstname", user.getFirstname());
                response.put("lastname", user.getLastname());
                response.put("username", user.getUsername());
                response.put("email", user.getEmail());
                response.put("role", user.getRole());
                response.put("avatarUrl", user.getAvatarUrl());
                response.put("accountStatus", user.getAccountStatus());
                response.put("lastSessionDate", user.getLastSessionDate());
                response.put("primaryCarePhysician", user.getPrimaryCarePhysician());
                response.put("nextAppointment", user.getNextAppointment());
                response.put("birth", user.getBirth());
                response.put("workingHours", user.getWorkingHours());
                response.put("contactNumber", user.getContactNumber());
                response.put("Specializations", user.getSpecializations());
                response.put("experienceYears", user.getExperienceYears());


                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading user");
        }
    }

    @PutMapping("/shared_D_A/updateUser/{username}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")

    public ResponseEntity<?> updateUser(
            @PathVariable String username,
            @Validated @RequestBody UserUpdateRequest updateRequest
    ) {
        try {
            // Pass null for newRole (non-admin users can't change roles)
            User updatedUser = userService.updateUser(username, updateRequest, null);
            return ResponseEntity.ok(updatedUser);
        } catch (UsernameNotFoundException | AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }



    @PutMapping("/shared_D_A/updateRole/{username}/role")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<?> updateUserRole(
            @PathVariable String username,
            @RequestParam AppRole newRole,
            @Validated @RequestBody UserUpdateRequest updateRequest

    ) {
        try {
            User updatedUser = userService.updateUser(username, updateRequest, newRole);
            return ResponseEntity.ok(updatedUser);
        } catch (UsernameNotFoundException | AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/admin/analytics/user-stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Integer>> getUserStats() {
        return ResponseEntity.ok(Map.of(
                "totalUsers", userService.countUsers(),
                "activeUsers", userService.countByAccountStatus("ACTIVE"),
                "inactiveUsers", userService.countByAccountStatus("INACTIVE"),
                "doctors", userService.countDoctors(),
                "patients", userService.countPatients()
        ));
    }

    @GetMapping("/shared_D_A/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String query) {
        return ResponseEntity.ok(userService.searchUsers(query));
    }

}