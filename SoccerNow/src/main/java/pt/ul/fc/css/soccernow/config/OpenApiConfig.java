package pt.ul.fc.css.soccernow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  private static final List<String> TAG_ORDER =
      Arrays.asList("Authentication", "User", "Player", "Referee");

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("SoccerNow API")
                .version("1.0")
                .description("API for managing football players, referees and matches"));
  }

  @Bean
  public OpenApiCustomizer sortTagsAlphabetically() {
    return openApi -> {
      List<Tag> tags = openApi.getTags();
      if (tags != null) {
        tags.sort(
            Comparator.comparingInt(
                tag -> {
                  int index = TAG_ORDER.indexOf(tag.getName());
                  return index == -1 ? Integer.MAX_VALUE : index;
                }));
      }
    };
  }
}
