package tn.esprit.mindfull.Controller;

import tn.esprit.mindfull.Dto.GroupRequest;
import tn.esprit.mindfull.Services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @PostMapping("/join")
    public void joinGroup(@RequestBody GroupRequest request) {
        groupService.joinGroup(request.getUserId(), request.getGroupId());
    }
}