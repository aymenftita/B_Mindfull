package tn.esprit.mindfull.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.GroupMembership;
import java.util.Optional;

@Repository
public interface GroupMembershipRepository extends JpaRepository<GroupMembership, Long> {
    Optional<GroupMembership> findByGroupNameAndUserId(String groupName, Long userId);
    void deleteByGroupName(String groupName);
    long countByGroupName(String groupName);
}