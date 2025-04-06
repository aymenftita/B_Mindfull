package tn.esprit.mindfull.Services;

import tn.esprit.mindfull.entity.Programs;
import tn.esprit.mindfull.entity.UserActivity;
import tn.esprit.mindfull.Respository.ProgramRepository;
import tn.esprit.mindfull.Respository.UserActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {
    @Autowired
    private UserActivityRepository userActivityRepository;
    @Autowired
    private ProgramRepository programRepository;

    public List<Programs> recommendPrograms(Long userId) {
        List<UserActivity> activities = userActivityRepository.findByUserId(userId);
        if (activities.isEmpty()) {
            return programRepository.findAll();
        }
        String latestMood = activities.get(activities.size() - 1).getMood();
        return programRepository.findByMoodTarget(latestMood);
    }
}