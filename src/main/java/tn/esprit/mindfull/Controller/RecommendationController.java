package tn.esprit.mindfull.Controller;

import tn.esprit.mindfull.entity.Programs;
import tn.esprit.mindfull.Services.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/{userId}")
    public List<Programs> getRecommendations(@PathVariable Long userId) {
        return recommendationService.recommendPrograms(userId);
    }
}