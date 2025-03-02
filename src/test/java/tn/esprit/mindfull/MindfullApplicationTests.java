package tn.esprit.mindfull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MindfullApplicationTests {

    @Test
    void contextLoads() {

        tn.esprit.mindfull.entity.Quizz_Test_Game.Test test = tn.esprit.mindfull.entity.Quizz_Test_Game.Test.builder().test_id(2L).build();

        System.out.println(test);
    }

}
