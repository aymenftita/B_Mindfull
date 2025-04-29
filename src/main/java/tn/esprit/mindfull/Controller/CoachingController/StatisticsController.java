package tn.esprit.mindfull.Controller.CoachingController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Repository.CoachingRepository.CoachingProgramRepository;

import java.util.*;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final CoachingProgramRepository coachingProgramRepository;

    /** Combine participants & contents en une seule liste par programme */
    @GetMapping("/programs")
    public List<Map<String,Object>> getProgramStatistics() {
        // 1) participants
        List<Object[]> part = coachingProgramRepository.getParticipantsByProgram();
        // 2) contenu
        List<Object[]> cont = coachingProgramRepository.getContentCountByProgram();

        // Map<titre, stats>
        Map<String,Map<String,Object>> stats = new LinkedHashMap<>();
        for(var row: part) {
            String title = (String)row[0];
            Number p = (Number)row[1];
            stats.putIfAbsent(title, new HashMap<>());
            stats.get(title).put("programTitle", title);
            stats.get(title).put("participantsCount", p.intValue());
        }
        for(var row: cont) {
            String title = (String)row[0];
            Number c = (Number)row[1];
            stats.putIfAbsent(title, new HashMap<>());
            stats.get(title).put("programTitle", title);
            stats.get(title).put("contentCount", c.intValue());
        }

        // Compléter les clés manquantes à 0
        for(var m: stats.values()) {
            m.putIfAbsent("participantsCount", 0);
            m.putIfAbsent("contentCount", 0);
        }

        return new ArrayList<>(stats.values());
    }
}
