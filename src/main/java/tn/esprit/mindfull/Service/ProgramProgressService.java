package tn.esprit.mindfull.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Respository.CoachingProgramRepository;
import tn.esprit.mindfull.Respository.ProgramContentRepository;
import tn.esprit.mindfull.Respository.ProgramProgressRepository;
import tn.esprit.mindfull.Respository.UserRepository;
import tn.esprit.mindfull.entity.CoachingProgram;
import tn.esprit.mindfull.entity.ProgramProgress;
import tn.esprit.mindfull.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProgramProgressService {

    @Autowired
    private ProgramProgressRepository progressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CoachingProgramRepository programRepository;
    @Autowired
    private ProgramContentRepository programContentRepository;

    public void markAsViewed(Long userId, Long programId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CoachingProgram program = programRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Program not found"));

        Optional<ProgramProgress> existingProgressOpt = progressRepository.findByUserAndProgram(user, program);

        ProgramProgress progress;
        if (existingProgressOpt.isPresent()) {
            progress = existingProgressOpt.get();
            progress.setViewsCount(progress.getViewsCount() + 1);
        } else {
            progress = new ProgramProgress();
            progress.setUser(user);
            progress.setProgram(program);
            progress.setViewsCount(1);
            progress.setProgressPercentage(0.0); // init
        }

        progress.setLastViewedDate(LocalDateTime.now());
        progress.setProgressPercentage(calculateProgress(program, user));

        progressRepository.save(progress);
    }


    public List<ProgramProgress> getProgressByProgram(Long programId) {
        return progressRepository.findByProgramId(programId);
    }

    private double calculateProgress(CoachingProgram program, User user) {
        int totalSteps = programContentRepository.countByProgramAndUsers(program, user);
        int completedSteps = programContentRepository.countByProgramAndUsersAndCompletedTrue(program, user);

        if (totalSteps == 0) return 0.0;

        return (completedSteps * 100.0) / totalSteps;
    }

    /**
     * Récupérer tous les programmes et leur progression pour un utilisateur spécifique
     * @param userId : ID de l'utilisateur pour lequel on récupère les progressions
     */
    public List<ProgramProgress> getAllProgramProgress(Long userId) {
        // Récupérer tous les programmes
        List<CoachingProgram> allPrograms = programRepository.findAll();
        List<ProgramProgress> progressList = new ArrayList<>();

        // Pour chaque programme, vérifier s'il existe une progression pour l'utilisateur
        for (CoachingProgram program : allPrograms) {
            // Vérifier s'il existe une progression pour cet utilisateur et ce programme
            Optional<ProgramProgress> existingProgressOpt = progressRepository.findByUserIdAndProgramId(userId, program.getId());

            ProgramProgress progress;
            if (existingProgressOpt.isPresent()) {
                progress = existingProgressOpt.get();
            } else {
                // Si aucune progression n'existe, créer une progression vide
                progress = new ProgramProgress();
                progress.setProgram(program);
                progress.setUser(new User()); // Assurez-vous de définir un utilisateur
                progress.setProgressPercentage(0.0); // Aucune progression donc 0%
            }

            // Ajouter cette progression à la liste
            progressList.add(progress);
        }

        return progressList;  // Retourner tous les programmes avec leur progression
    }

    public List<ProgramProgress> getProgressByProgramAndPatient(Long programId) {
        CoachingProgram program = programRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Program not found"));

        return progressRepository.findByProgramId(programId);
    }
}
