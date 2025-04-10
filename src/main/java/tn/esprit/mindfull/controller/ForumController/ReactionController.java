package tn.esprit.mindfull.controller.ForumController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.mindfull.entity.forum.Reaction;
import tn.esprit.mindfull.service.ForumService.ReactionService;

@RestController
@RequestMapping("/forum/reactions")
public class ReactionController {
    @Autowired
    private ReactionService reactionService;

    @PostMapping
    public Reaction addReaction(@RequestBody Reaction reaction) { return reactionService.saveReaction(reaction); }

}
