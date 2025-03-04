package tn.esprit.mindfull.Service.QuizzTestServive;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Respository.QuizzTestRepository.TestRepository;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Test;

@Service
public class TestService {

    private final TestRepository testRepository;

    @Autowired
    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public Test createTest(Test test) {
        return testRepository.save(test);
    }

    public Test getTest(Long id) {
        return testRepository.findById(id).orElse(null);
    }
}
