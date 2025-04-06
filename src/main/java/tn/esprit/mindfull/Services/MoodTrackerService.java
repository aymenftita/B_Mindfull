package tn.esprit.mindfull.Services;

import tn.esprit.mindfull.entity.UserActivity;
import tn.esprit.mindfull.Respository.UserActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MoodTrackerService {
    @Autowired
    private UserActivityRepository userActivityRepository;

    public UserActivity logMood(Long userId, String mood, Integer intensity, String notes) {
        UserActivity activity = new UserActivity();
        activity.setUserId(userId);
        activity.setMood(mood);
        activity.setIntensity(intensity);
        activity.setNotes(notes);
        activity.setTimestamp(LocalDateTime.now());
        return userActivityRepository.save(activity);
    }

    public List<UserActivity> getMoodHistory(Long userId) {
        return userActivityRepository.findByUserId(userId);
    }
}