package tn.esprit.mindfull.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.mindfull.entity.Message;

import java.util.List;
import java.util.Set;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    List<Message> findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
            Long senderId1, Long receiverId1, Long senderId2, Long receiverId2);

    List<Message> findByGroupName(String groupName);

    @Query("SELECT DISTINCT m.sender.id FROM Message m WHERE m.receiver.id = :userId " +
            "UNION " +
            "SELECT DISTINCT m.receiver.id FROM Message m WHERE m.sender.id = :userId AND m.receiver.id IS NOT NULL")
    Set<Long> findDistinctConversationUserIds(Long userId);
}