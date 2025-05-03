package pt.ul.fc.css.soccernow;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@OpenAPIDefinition(
    info =
        @Info(
            title = "SoccerNow API",
            version = "1.0",
            description = "API for managing football players, referees and matches"))
public class SoccerNowApplication implements WebMvcConfigurer {

  public static void main(String[] args) {
    SpringApplication.run(SoccerNowApplication.class, args);
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {

    registry.addRedirectViewController("/", "/swagger-ui.html");
  }

  @Bean
  @Transactional
  public CommandLineRunner demo() {
    return (args) -> {
      System.out.println("Application started successfully!");
      System.out.println("Swagger UI available at: http://localhost:8080/swagger-ui.html");
      System.out.println("API Docs available at: http://localhost:8080/v3/api-docs");
    };
  }
}
