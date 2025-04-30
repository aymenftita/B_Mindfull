package tn.esprit.mindfull.Service.CoachingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Repository.CoachingRepository.CoachingProgramRepository;
import tn.esprit.mindfull.Repository.UserRepository.UserRepository;
import tn.esprit.mindfull.entity.Coaching.CoachingProgram;
import tn.esprit.mindfull.entity.User.Role;
import tn.esprit.mindfull.entity.User.User;

import java.util.List;

@Service
public class CoachingProgramServiceImpl implements ICoachingProgramService {

    private final CoachingProgramRepository repository;
    @Autowired
    private UserRepository userRepository;

    public CoachingProgramServiceImpl(CoachingProgramRepository repository) {
        this.repository = repository;

    }

    @Override
    public List<CoachingProgram> getAllPrograms() {
        return repository.findAll();
    }

    @Override
    public CoachingProgram getProgramById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public CoachingProgram saveProgram(CoachingProgram program) {
        CoachingProgram savedProgram = repository.save(program);



        return savedProgram;

    }
    public CoachingProgram updateProgram(Long id, CoachingProgram programDetails) {
        CoachingProgram program = repository.findById(id).orElseThrow();
        program.setTitle(programDetails.getTitle());
        program.setTitle(programDetails.getTitle());
        program.setDescription(programDetails.getDescription());
        program.setStartDate(programDetails.getStartDate());
        program.setEndDate(programDetails.getEndDate());
        program.setParticipants(programDetails.getParticipants());
        return repository.save(program); // Sauvegarde explicite
    }

    @Override
    public void deleteProgram(Long id) {
        repository.deleteById(id);
    }

    @Override
    public CoachingProgram createProgramWithInitialProgress(CoachingProgram program) {
        User coach = userRepository.findByRole(Role.valueOf("COACH")).stream().findFirst().orElse(null);
        program.setCoach(coach);
        return repository.save(program);
    }
    public CoachingProgram createProgram(CoachingProgram program) {
        // Récupérer l'utilisateur avec le rôle "coach"
        User user = (User) userRepository.findByRole(Role.COACH);

        if (user != null) {
            program.setCoach(user);  // Assigner le coach au programme de coaching
        } else {
            throw new IllegalArgumentException("Aucun coach disponible pour ce programme.");
        }

        // Sauvegarder le programme de coaching
        return repository.save(program);
    }



    // Méthode pour compter les programmes par coach
    public Long countProgramsByCoach(Long coachId) {
        return repository.countByCoachId(coachId);
    }
   /* public Long getCoachWithMostPrograms() {
        return repository.findCoachIdWithMostPrograms();
    }*/




}
