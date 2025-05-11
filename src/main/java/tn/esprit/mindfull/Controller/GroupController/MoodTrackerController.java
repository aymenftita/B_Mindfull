package tn.esprit.mindfull.Controller.GroupController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tn.esprit.mindfull.Repository.UserRepository.UserRepository;
import tn.esprit.mindfull.entity.Group.Program;
import tn.esprit.mindfull.entity.Group.UserActivity;
import tn.esprit.mindfull.entity.Group.Goal;
import tn.esprit.mindfull.entity.Group.Achievement;
import tn.esprit.mindfull.Repository.GroupRepository.ProgramRepository;
import tn.esprit.mindfull.Repository.GroupRepository.UserActivityRepository;
import tn.esprit.mindfull.Repository.GroupRepository.GoalRepository;
import tn.esprit.mindfull.Repository.GroupRepository.AchievementRepository;
import tn.esprit.mindfull.Service.GroupService.UserActivityService;
import tn.esprit.mindfull.entity.User.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mood-tracker")
@RequiredArgsConstructor
public class MoodTrackerController {

    private final UserActivityRepository userActivityRepository;
    private final ProgramRepository programRepository;
    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final AchievementRepository achievementRepository;
    private final UserActivityService userActivityService;
    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/log-entry")
    public ResponseEntity<UserActivity> logMoodEntry(
            @RequestParam Long userId,
            @RequestParam String mood,
            @RequestParam Integer intensity,
            @RequestParam(required = false) String notes) {
        User User = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        UserActivity userActivity = new UserActivity();
        userActivity.setUser(User);
        userActivity.setMood(mood);
        userActivity.setIntensity(intensity);
        userActivity.setNotes(notes);
        userActivity.setTimestamp(LocalDateTime.now());
        UserActivity saved = userActivityRepository.save(userActivity);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserActivity>> getMoodEntries(@PathVariable Long userId) {
        return ResponseEntity.ok(userActivityRepository.findByUserId(userId));
    }

    @GetMapping("/recommend/{userId}")
    public ResponseEntity<Program> recommendProgram(@PathVariable Long userId) {
        List<UserActivity> activities = userActivityRepository.findByUserId(userId);
        if (activities.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        List<String> texts = activities.stream()
                .map(UserActivity::getNotes)
                .filter(notes -> notes != null && !notes.isEmpty())
                .collect(Collectors.toList());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("texts", texts);
        Map<String, Object> response = restTemplate.postForObject(
                "http://localhost:5001/analyze",
                requestBody,
                Map.class
        );

        if (response == null) {
            return ResponseEntity.status(500).body(null);
        }

        String recommendation = (String) response.get("recommendation");
        String predictedTrend = (String) response.get("predicted_trend");

        List<Goal> goals = goalRepository.findByUserIdAndCompleted(userId, false);
        for (Goal goal : goals) {
            int priority = 3;
            if ("negative".equals(predictedTrend)) {
                if (goal.getDescription().toLowerCase().contains("meditate") ||
                        goal.getDescription().toLowerCase().contains("breathe") ||
                        goal.getDescription().toLowerCase().contains("relax")) {
                    priority = 1;
                }
            } else if ("positive".equals(predictedTrend)) {
                if (goal.getDescription().toLowerCase().contains("exercise") ||
                        goal.getDescription().toLowerCase().contains("journal")) {
                    priority = 1;
                }
            }
            goal.setPriority(priority);
            goalRepository.save(goal);
        }

        Program program = programRepository.findByTitleContainingIgnoreCase(recommendation);
        if (program == null) {
            program = new Program();
            program.setProgramId(System.currentTimeMillis());
            program.setCoachId(1L);
            program.setTitle(recommendation);
            program.setDescription("A program tailored for " + recommendation);
            program.setStartDate(LocalDateTime.now());
            program.setEndDate(LocalDateTime.now().plusMonths(1));
            program.setParticipants(1);
            program.setVersion(1L);
            program = programRepository.save(program);
        }

        return ResponseEntity.ok(program);
    }

    @PostMapping("/goals")
    public ResponseEntity<Goal> setGoal(
            @RequestParam Long userId,
            @RequestParam String description) {
        User User = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Goal goal = new Goal();
        goal.setUser(User);
        goal.setDescription(description);
        goal.setCreatedAt(LocalDateTime.now());
        goal.setCompleted(false);
        goal.setPriority(3);
        Goal saved = goalRepository.save(goal);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/goals/{userId}")
    public ResponseEntity<List<Goal>> getGoals(@PathVariable Long userId) {
        List<Goal> goals = goalRepository.findByUserId(userId);
        goals.sort((g1, g2) -> {
            int priorityCompare = Integer.compare(g1.getPriority(), g2.getPriority());
            if (priorityCompare != 0) {
                return priorityCompare;
            }
            return g2.getCreatedAt().compareTo(g1.getCreatedAt());
        });
        return ResponseEntity.ok(goals);
    }

    @PutMapping("/goals/complete/{goalId}")
    public ResponseEntity<Goal> completeGoal(@PathVariable Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found with ID: " + goalId));
        goal.setCompleted(true);
        goal.setCompletedAt(LocalDateTime.now());
        Goal updated = goalRepository.save(goal);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/achievements/{userId}")
    public ResponseEntity<List<Achievement>> getAchievements(@PathVariable Long userId) {
        return ResponseEntity.ok(userActivityService.getAchievements(userId));
    }
}