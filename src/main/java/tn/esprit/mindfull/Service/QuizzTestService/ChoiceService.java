package tn.esprit.mindfull.Service.QuizzTestService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Respository.QuizzTestRepository.ChoiceRepository;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Choice;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Score;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChoiceService {
    private final ChoiceRepository choiceRepository;

    public List<Choice> getAllChoices(){return choiceRepository.findAll();}

    public Optional<Choice> getChoiceById(int id){return choiceRepository.findById((long) id);}

    public Choice saveChoice(Choice choice) {
        return choiceRepository.save(choice);
    }

    public void deleteChoice(long id) {choiceRepository.deleteById(id);}



}
