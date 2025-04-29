package tn.esprit.mindfull.dto.Groupdto;

import lombok.Data;

import java.util.List;

@Data
public class GroupAssignmentDTO {
    private String groupName;
    private List<Long> userIds;
}
