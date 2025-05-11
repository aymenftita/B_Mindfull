package tn.esprit.mindfull.Controller.QuizTestGameController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Service.QuizzTestService.TestService;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Test;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @GetMapping
    public List<Test> getAllTest(){
        return testService.getTests();
    }

    @GetMapping("/{id}")
    public Test getTestById(@PathVariable long id){
        return testService.getTest(id);
    }

    @PostMapping
    public Test createTest(@RequestBody Test test){
        return testService.createTest(test);
    }

    @DeleteMapping("/{id}")
    public void deleteTest(@PathVariable long id){
         testService.deleteTest(id);
    }

    @PutMapping("/{id}")
    public Test updateTest(@RequestBody Test test,@PathVariable long id){
        return testService.updateTest(id,test);
    }

}
