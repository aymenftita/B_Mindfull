package tn.esprit.mindfull.Service;

import tn.esprit.mindfull.entity.CoachingProgram;

import java.util.List;

public interface ICoachingProgramService {
    List<CoachingProgram> getAllPrograms();
    CoachingProgram getProgramById(Long id);
    CoachingProgram saveProgram(CoachingProgram program);
    void deleteProgram(Long id);


    CoachingProgram createProgramWithInitialProgress(CoachingProgram program);



}
