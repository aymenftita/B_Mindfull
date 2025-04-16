package tn.esprit.mindfull.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Respository.CoachRepository;
import tn.esprit.mindfull.Respository.CoachingProgramRepository;
import tn.esprit.mindfull.entity.Coach;
import tn.esprit.mindfull.entity.CoachingProgram;

import java.util.*;

@Service
public class CoachStatisticsService {

    @Autowired
    private CoachingProgramRepository coachingProgramRepository;

    @Autowired
    private CoachRepository coachRepository;


    public Coach getCoachWithMostPrograms() {
        List<Coach> result = coachRepository.findCoachWithMostPrograms();
        if (result.isEmpty()) {
            return null; // Ou loguez un message d'erreur
        }
        return result.get(0); // Le coach avec le plus de programmes
    }


}

