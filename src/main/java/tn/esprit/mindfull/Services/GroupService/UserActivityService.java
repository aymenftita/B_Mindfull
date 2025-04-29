package tn.esprit.mindfull.Services.GroupService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.entity.Group.UserActivity;
import tn.esprit.mindfull.entity.Group.Achievement;
import tn.esprit.mindfull.Repository.GroupRepository.UserActivityRepository;
import tn.esprit.mindfull.Repository.GroupRepository.AchievementRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserActivityService {

    private final UserActivityRepository userActivityRepository;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserActivityService.class);

    @Transactional
    public UserActivity logMoodEntry(Long userId, String mood, Integer intensity, String notes) {
        logger.info("Logging mood entry for user ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        UserActivity activity = new UserActivity();
        activity.setMood(mood);
        activity.setIntensity(intensity);
        activity.setNotes(notes);
        activity.setTimestamp(LocalDateTime.now());
        activity.setUser(user);

        UserActivity saved = userActivityRepository.save(activity);
        logger.info("Mood entry saved with ID: {}", saved.getId());

        logger.info("Calling checkAchievements after logging mood for user ID: {}", userId);
        checkAchievements(user);

        return saved;
    }

    @Transactional
    public List<UserActivity> getUserMoodHistory(Long userId) {
        logger.info("Fetching mood history for user ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        List<UserActivity> activities = userActivityRepository.findByUserId(userId);
        logger.info("Found {} activities for user ID: {}", activities.size(), userId);
        logger.info("Activities timestamps: {}", activities.stream().map(UserActivity::getTimestamp).collect(Collectors.toList()));
        logger.info("Calling checkAchievements for user ID: {}", userId);
        checkAchievements(user);
        return activities;
    }

    private void checkAchievements(User user) { // Removed @Transactional
        try {
            logger.info("Starting checkAchievements for user ID: {}", user.getId());
            List<UserActivity> activities = userActivityRepository.findByUserId(user.getId());
            List<Achievement> existingAchievements = achievementRepository.findByUserId(user.getId());

            logger.info("User ID: {}. Number of activities: {}, Existing achievements: {}",
                    user.getId(), activities.size(), existingAchievements.size());

            // Achievement: First Mood Log
            if (activities.size() >= 1) {
                if (!existingAchievements.stream().anyMatch(a -> a.getTitle().equals("First Mood Log"))) {
                    Achievement achievement = new Achievement();
                    achievement.setUser(user);
                    achievement.setTitle("First Mood Log");
                    achievement.setDescription("Logged your first mood!");
                    achievement.setAchievedAt(LocalDateTime.now());
                    achievementRepository.save(achievement);
                    logger.info("Awarded 'First Mood Log' achievement for user ID: {}", user.getId());
                } else {
                    logger.info("'First Mood Log' achievement already awarded for user ID: {}", user.getId());
                }
            }

            int streak = calculateStreak(activities);
            logger.info("Calculated streak for user ID: {}: {}", user.getId(), streak);

            String[] streakAchievements = {"3-Day Streak", "7-Day Streak", "14-Day Streak"};
            int[] streakThresholds = {3, 7, 14};

            for (int i = 0; i < streakAchievements.length; i++) {
                int finalI = i;
                if (streak >= streakThresholds[i] && !existingAchievements.stream().anyMatch(a -> a.getTitle().equals(streakAchievements[finalI]))) {
                    Achievement achievement = new Achievement();
                    achievement.setUser(user);
                    achievement.setTitle(streakAchievements[i]);
                    achievement.setDescription("Logged moods for " + streakThresholds[i] + " consecutive days!");
                    achievement.setAchievedAt(LocalDateTime.now());
                    achievementRepository.save(achievement);
                    logger.info("Awarded '{}' achievement for user ID: {}", streakAchievements[i], user.getId());
                } else {
                    logger.info("Did not award '{}': streak = {}, threshold = {}, already awarded = {}",
                            streakAchievements[i], streak, streakThresholds[i],
                            existingAchievements.stream().anyMatch(a -> a.getTitle().equals(streakAchievements[finalI])));
                }
            }
        } catch (Exception e) {
            logger.error("Error in checkAchievements for user ID: {}. Error: {}", user.getId(), e.getMessage(), e);
            throw new RuntimeException("Failed to check achievements", e);
        }
    }

    private int calculateStreak(List<UserActivity> activities) {
        if (activities.isEmpty()) {
            logger.info("No activities found, streak is 0");
            return 0;
        }

        activities.sort((a1, a2) -> a2.getTimestamp().compareTo(a1.getTimestamp()));

        // For testing: Treat each minute as a day
        List<Long> minuteOffsets = activities.stream()
                .map(activity -> {
                    LocalDateTime baseTime = activities.get(activities.size() - 1).getTimestamp();
                    return ChronoUnit.MINUTES.between(baseTime, activity.getTimestamp());
                })
                .distinct()
                .sorted((m1, m2) -> m2.compareTo(m1))
                .collect(Collectors.toList());

        if (minuteOffsets.isEmpty()) {
            logger.info("No minute offsets found after mapping, streak is 0");
            return 0;
        }

        int streak = 1;
        long currentMinuteOffset = minuteOffsets.get(0);

        logger.info("Minute offsets for streak calculation: {}", minuteOffsets);

        for (int i = 1; i < minuteOffsets.size(); i++) {
            long previousMinuteOffset = minuteOffsets.get(i);
            long minutesBetween = currentMinuteOffset - previousMinuteOffset;
            logger.info("Comparing minute offsets: {} and {}, minutes between: {}", previousMinuteOffset, currentMinuteOffset, minutesBetween);
            if (minutesBetween == 1) {
                streak++;
                currentMinuteOffset = previousMinuteOffset;
            } else if (minutesBetween > 1) {
                logger.info("Minutes between exceeds 1, breaking streak at: {}", streak);
                break;
            }
        }

        return streak;
    }

    @Transactional
    public List<Achievement> getAchievements(Long userId) {
        logger.info("Fetching achievements for user ID: {}", userId);
        List<Achievement> achievements = achievementRepository.findByUserId(userId);
        logger.info("Found {} achievements for user ID: {}", achievements.size(), userId);
        return achievements;
    }
}