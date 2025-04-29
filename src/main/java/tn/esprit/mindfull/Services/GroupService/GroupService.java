package tn.esprit.mindfull.Services.GroupService;

import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Dto.GroupAssignmentDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GroupService {

    private final UserRepository userRepository;
    private final Map<String, List<Long>> groupMapping = new HashMap<>();

    public GroupService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void assignUsersToGroup(GroupAssignmentDTO dto) {
        groupMapping.put(dto.getGroupName(), dto.getUserIds());
    }

    public List<Long> getUsersInGroup(String groupName) {
        return groupMapping.getOrDefault(groupName, List.of());
    }
}
