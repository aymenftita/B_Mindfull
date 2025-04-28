package tn.esprit.mindfull.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.entity.UserActivity;
import tn.esprit.mindfull.Services.UserActivityService;

import java.util.List;

@RestController
@RequestMapping("/api/mood-tracker")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") // Added to allow frontend requests
public class UserActivityController {

    private final UserActivityService userActivityService;

    // Log a new mood entry (accessible to authenticated users)
    @PostMapping("/log")
    public ResponseEntity<UserActivity> logMoodEntry(
            @RequestParam Long userId,
            @RequestParam String mood,
            @RequestParam Integer intensity,
            @RequestParam(required = false) String notes) {
        UserActivity activity = userActivityService.logMoodEntry(userId, mood, intensity, notes);
        return ResponseEntity.ok(activity);
    }

    // Fetch mood history for a user (accessible to the user themselves)
    @GetMapping("/history")
    public ResponseEntity<List<UserActivity>> getUserMoodHistory(@RequestParam Long userId) {
        List<UserActivity> history = userActivityService.getUserMoodHistory(userId);
        return ResponseEntity.ok(history);
    }

    // Fetch mood history for a specific user (accessible to admins only)
    @GetMapping("/admin/logs/{userId}")
    public ResponseEntity<List<UserActivity>> getUserLogsForAdmin(@PathVariable Long userId) {
        List<UserActivity> logs = userActivityService.getUserMoodHistory(userId);
        return ResponseEntity.ok(logs);
    }
}