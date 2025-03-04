package tn.esprit.mindfull.Respository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Respository.QuizzTestRepository.TestRepository;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Test;

import java.util.List;

@Service
public class UserRepository {

    @Autowired
    private TestRepository testRepository;

    public List<Test> getTestRepository() {
        return testRepository.findAll();
    }
}
