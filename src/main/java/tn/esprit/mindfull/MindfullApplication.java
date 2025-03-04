package tn.esprit.mindfull;

import fun.mike.dmp.Diff;
import fun.mike.dmp.DiffMatchPatch;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Test;

import java.util.LinkedList;

@SpringBootApplication
public class MindfullApplication {

    public static void main(String[] args) {
        SpringApplication.run(MindfullApplication.class, args);

        String s1 = "Hello World";
        String s2 = "Helo Wor";


        DiffMatchPatch dmp = new DiffMatchPatch();
        LinkedList<Diff> diffs = dmp.diff_main(s1, s2);
        System.out.println(diffs);
    }

}
