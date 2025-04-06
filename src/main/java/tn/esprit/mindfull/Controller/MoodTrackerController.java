package tn.esprit.mindfull.Controller;


import tn.esprit.mindfull.Dto.MoodTrackerRequest;
import tn.esprit.mindfull.entity.UserActivity;
import tn.esprit.mindfull.Services.MoodTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mood")
public class MoodTrackerController {
    @Autowired
    private MoodTrackerService moodTrackerService;

    @PostMapping("/log")
    public UserActivity logMood(@RequestBody MoodTrackerRequest request) {
        return moodTrackerService.logMood(request.getUserId(), request.getMood(),
                request.getIntensity(), request.getNotes());
    }

    @GetMapping("/history/{userId}")
    public List<UserActivity> getMoodHistory(@PathVariable Long userId) {
        return moodTrackerService.getMoodHistory(userId);
    }
}