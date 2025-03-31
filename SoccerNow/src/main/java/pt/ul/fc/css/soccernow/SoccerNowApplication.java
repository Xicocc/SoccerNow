package pt.ul.fc.css.soccernow;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import jakarta.transaction.Transactional;

@SpringBootApplication
public class SoccerNowApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoccerNowApplication.class, args);

    }

    @Bean
    @Transactional
    public CommandLineRunner demo() {
        return (args) -> {
            System.out.println("do some sanity tests here");
        };
    }
}
