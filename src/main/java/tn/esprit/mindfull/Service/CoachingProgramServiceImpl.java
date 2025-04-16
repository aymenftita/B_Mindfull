package tn.esprit.mindfull.Service;

import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Respository.CoachRepository;
import tn.esprit.mindfull.Respository.CoachingProgramRepository;
import tn.esprit.mindfull.entity.Coach;
import tn.esprit.mindfull.entity.CoachingProgram;

import java.util.List;

@Service
public class CoachingProgramServiceImpl implements ICoachingProgramService {

    private final CoachingProgramRepository repository;
    private CoachRepository coachRepository;

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
        // ... autres setters
        return repository.save(program); // Sauvegarde explicite
    }

    @Override
    public void deleteProgram(Long id) {
        repository.deleteById(id);
    }

    @Override
    public CoachingProgram createProgramWithInitialProgress(CoachingProgram program) {
        return null;
    }


}
