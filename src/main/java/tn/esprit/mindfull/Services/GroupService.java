package  tn.esprit.mindfull.Services;

import tn.esprit.mindfull.entity.UserActivity;
import tn.esprit.mindfull.Respository.UserActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    private UserActivityRepository userActivityRepository;

    public void joinGroup(Long userId, Long groupId) {
        List<UserActivity> activities = userActivityRepository.findByUserId(userId);
        UserActivity latest = activities.isEmpty() ? new UserActivity() : activities.get(activities.size() - 1);
        if (latest.getUserId() == null) latest.setUserId(userId);
        String groupIds = latest.getGroupIds() == null ? "" : latest.getGroupIds();
        if (!groupIds.contains(String.valueOf(groupId))) {
            latest.setGroupIds(groupIds + (groupIds.isEmpty() ? "" : ",") + groupId);
            userActivityRepository.save(latest);
        }
    }
}