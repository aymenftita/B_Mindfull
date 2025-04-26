package tn.esprit.mindfull;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
public class MindfullApplication {

    public static void main(String[] args) {
        SpringApplication.run(MindfullApplication.class, args);
    }
}
