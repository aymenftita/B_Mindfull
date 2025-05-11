package tn.esprit.mindfull.Controller.GroupController;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tn.esprit.mindfull.entity.Group.GroupMembership;
import tn.esprit.mindfull.entity.Group.Message;
import tn.esprit.mindfull.Repository.GroupRepository.GroupMembershipRepository;
import tn.esprit.mindfull.Service.GroupService.MessageService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final GroupMembershipRepository groupMembershipRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Set<String> groupNames = ConcurrentHashMap.newKeySet();

    @Bean
    public CommandLineRunner initializeGroups() {
        return args -> {
            List<String> existingGroups = groupMembershipRepository.findAll()
                    .stream()
                    .map(GroupMembership::getGroupName)
                    .distinct()
                    .collect(Collectors.toList());
            groupNames.addAll(existingGroups);
            System.out.println("Loaded groups from database: " + groupNames);
        };
    }

    private void broadcastGroupUpdate() {
        messagingTemplate.convertAndSend("/topic/groups", "refresh");
    }

    @GetMapping("/history/private")
    public ResponseEntity<List<Message>> getPrivateChatHistory(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {
        return ResponseEntity.ok(messageService.getChatHistoryBetweenUsers(senderId, receiverId));
    }

    @GetMapping("/history/group")
    public ResponseEntity<List<Message>> getGroupChatHistory(@RequestParam String groupName) {
        return ResponseEntity.ok(messageService.getChatHistoryByGroup(groupName));
    }

    @PostMapping("/group/create")
    public ResponseEntity<String> createGroup(@RequestParam String groupName) {
        if (groupName == null || groupName.isBlank()) {
            return ResponseEntity.badRequest().body("Group name cannot be empty.");
        }
        if (!groupNames.add(groupName)) {
            return ResponseEntity.badRequest().body("Group already exists.");
        }
        broadcastGroupUpdate();
        return ResponseEntity.ok("Group '" + groupName + "' created successfully.");
    }

    @GetMapping("/groups")
    public ResponseEntity<Set<String>> getAllGroups() {
        return ResponseEntity.ok(groupNames);
    }

    @GetMapping("/group/members")
    public ResponseEntity<Map<String, List<Long>>> getGroupMembers() {
        List<GroupMembership> memberships = groupMembershipRepository.findAll();
        Map<String, List<Long>> groupMembers = new HashMap<>();

        for (String groupName : groupNames) {
            groupMembers.put(groupName, new ArrayList<>());
        }

        for (GroupMembership membership : memberships) {
            String groupName = membership.getGroupName();
            Long userId = membership.getUserId();
            groupMembers.computeIfAbsent(groupName, k -> new ArrayList<>()).add(userId);
        }

        return ResponseEntity.ok(groupMembers);
    }

    @GetMapping("/conversations")
    public ResponseEntity<Set<Long>> getConversations(@RequestParam Long userId) {
        return ResponseEntity.ok(messageService.getConversationUserIds(userId));
    }

    @PostMapping("/group/assign")
    public ResponseEntity<String> assignUserToGroup(
            @RequestParam String groupName,
            @RequestParam Long userId) {
        try {
            groupNames.add(groupName);
            messageService.assignUserToGroup(groupName, userId);
            broadcastGroupUpdate();
            return ResponseEntity.ok("User " + userId + " assigned to group '" + groupName + "' successfully.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/group/remove")
    public ResponseEntity<String> removeUserFromGroup(
            @RequestParam String groupName,
            @RequestParam Long userId) {
        try {
            Optional<GroupMembership> membership = groupMembershipRepository
                    .findByGroupNameAndUserId(groupName, userId);
            if (membership.isPresent()) {
                groupMembershipRepository.delete(membership.get());
                broadcastGroupUpdate();
                return ResponseEntity.ok("User " + userId + " removed from group '" + groupName + "' successfully.");
            } else {
                return ResponseEntity.badRequest().body("User " + userId + " is not a member of group '" + groupName + "'.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to remove user from group: " + e.getMessage());
        }
    }

    @PostMapping("/group/delete")
    public ResponseEntity<String> deleteGroup(@RequestParam String groupName) {
        if (!groupNames.contains(groupName)) {
            return ResponseEntity.badRequest().body("Group '" + groupName + "' does not exist.");
        }
        long deletedCount = groupMembershipRepository.countByGroupName(groupName);
        System.out.println("Found " + deletedCount + " group membership records for group: " + groupName);
        groupMembershipRepository.deleteByGroupName(groupName);
        groupNames.remove(groupName);
        broadcastGroupUpdate();
        return ResponseEntity.ok("Group '" + groupName + "' deleted successfully.");
    }

    @MessageMapping("/private")
    public void sendPrivateMessage(@Payload Message message) {
        message.setTimestamp(LocalDateTime.now());
        Message saved = messageService.saveMessage(message);
        messagingTemplate.convertAndSendToUser(
                String.valueOf(message.getReceiver().getId()),
                "/queue/messages",
                saved
        );
    }

    @MessageMapping("/group/{groupName}")
    public void sendGroupMessage(@DestinationVariable String groupName, @Payload Message message) {
        message.setGroupName(groupName);
        message.setTimestamp(LocalDateTime.now());
        Message saved = messageService.saveMessage(message);
        messagingTemplate.convertAndSend("/topic/group/" + groupName, saved);
    }

    @MessageMapping("/broadcast")
    public void broadcastMessage(@Payload Message message) {
        message.setTimestamp(LocalDateTime.now());
        Message saved = messageService.saveMessage(message);
        messagingTemplate.convertAndSend("/topic/messages", saved);
    }

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, String>> analyzeMessage(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        if (message == null || message.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Message cannot be empty"));
        }

        Map<String, String> requestBody = Map.of("text", message);
        Map<String, Object> response = restTemplate.postForObject(
                "http://localhost:5001/analyze-sentiment",
                requestBody,
                Map.class
        );

        if (response == null) {
            return ResponseEntity.status(500).body(Map.of("error", "Analysis service unavailable"));
        }

        // Extract the response field and return it
        String botResponse = response.get("response") != null ? response.get("response").toString() : "I'm here to help. How can I assist you?";
        return ResponseEntity.ok(Map.of("response", botResponse));
    }
}