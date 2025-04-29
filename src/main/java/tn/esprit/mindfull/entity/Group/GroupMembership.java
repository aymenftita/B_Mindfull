package tn.esprit.mindfull.entity.Group;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_membership")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "user_id")
    private Long userId;
}