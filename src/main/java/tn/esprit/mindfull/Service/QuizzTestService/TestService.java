package tn.esprit.mindfull.Service.QuizzTestService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Repository.QuizzTestRepository.TestRepository;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Test;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {

    private final   TestRepository testRepository;


    public Test createTest(Test test) {
        return testRepository.save(test);
    }

    public Test getTest(Long id) {
        return testRepository.findById(id).orElse(null);
    }

    public List<Test> getTests() {return testRepository.findAll();}

    public void deleteTest(Long id) {testRepository.deleteById(id);}

    public Test updateTest(Long id, Test testUpdates) {
        return testRepository.findById(id)
                .map(existingTest -> {

                    existingTest.setName(testUpdates.getName());

                    return testRepository.save(existingTest);
                })
                .orElse(null);
    }
}
