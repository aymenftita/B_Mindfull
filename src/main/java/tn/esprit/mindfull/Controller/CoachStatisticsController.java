package tn.esprit.mindfull.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.mindfull.Service.CoachStatisticsService;
import tn.esprit.mindfull.entity.Coach;

@RestController
@RequestMapping("/stats")
public class CoachStatisticsController {

    @Autowired
    private CoachStatisticsService coachStatisticsService;

    @GetMapping("/coach-of-the-month")
    public ResponseEntity<Coach> getCoachWithMostPrograms() {
        Coach coach = coachStatisticsService.getCoachWithMostPrograms();
        if (coach != null) {
            return ResponseEntity.ok(coach);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

  /* @GetMapping("/coach-of-the-month")
   public String testCoach() {
       return "Coach trouvé !";}*/
}

