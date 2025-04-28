package tn.esprit.mindfull.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.entity.AppUser;
import tn.esprit.mindfull.entity.GroupMembership;
import tn.esprit.mindfull.entity.Message;
import tn.esprit.mindfull.Respository.GroupMembershipRepository;
import tn.esprit.mindfull.Respository.MessageRepository;
import tn.esprit.mindfull.Respository.UserRepository;
import java.util.List;
import java.util.Set;
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final GroupMembershipRepository groupMembershipRepository;

    public Message saveMessage(Message message) {
        System.out.println("Saving message to database: " + message);
        Message saved = messageRepository.save(message);
        System.out.println("Message saved to database: " + saved);
        return saved;
    }

    public List<Message> getChatHistoryBetweenUsers(Long senderId, Long receiverId) {
        return messageRepository.findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
                senderId, receiverId, receiverId, senderId);
    }

    public List<Message> getChatHistoryByGroup(String groupName) {
        return messageRepository.findByGroupName(groupName);
    }

    public Set<Long> getConversationUserIds(Long userId) {
        return messageRepository.findDistinctConversationUserIds(userId);
    }

    // New method to assign a user to a group
    public void assignUserToGroup(String groupName, Long userId) {
        // Check if the user is already in the group
        if (groupMembershipRepository.findByGroupNameAndUserId(groupName, userId).isPresent()) {
            throw new IllegalStateException("User is already a member of the group: " + groupName);
        }

        // Create a new GroupMembership entry
        GroupMembership membership = new GroupMembership();
        membership.setGroupName(groupName);
        membership.setUserId(userId); // Use setUserId instead of setUser

        groupMembershipRepository.save(membership);
    }
}