package tn.esprit.mindfull.Controller.GroupController;

import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Dto.GroupAssignmentDTO;
import tn.esprit.mindfull.Services.GroupService.GroupService;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/groups")
public class AdminGroupController {

    private final GroupService groupService;

    @PostMapping("/assign")
    public void assignUsersToGroup(@RequestBody GroupAssignmentDTO dto) {
        groupService.assignUsersToGroup(dto);
    }
}