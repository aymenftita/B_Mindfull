package tn.esprit.mindfull.Dto;

import lombok.Data;

import java.util.List;

@Data
public class GroupAssignmentDTO {
    private String groupName;
    private List<Long> userIds;
}
