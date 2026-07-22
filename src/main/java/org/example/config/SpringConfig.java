package org.example.config;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import lombok.Getter;
import org.example.model.service.dto.request.auth.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Getter
@Configuration
@ComponentScan(basePackages = {"org.example"})
public class SpringConfig {

  private final Environment env;

  public SpringConfig(Environment env) {
    this.env = env;
  }

  @Value("${booker.base-url}")
  private String baseUrl;

  @Value("${booker.auth.username}")
  private String username;

  @Value("${booker.auth.password}")
  private String password;

  @Bean
  public User defaultUser() {
    return User.builder().username(username).password(password).build();
  }

  @PostConstruct
  public void validateProfiles() {
    String[] activeProfiles = env.getActiveProfiles();
    if (activeProfiles.length == 0) {
      activeProfiles = env.getDefaultProfiles();
    }

    boolean isValid =
        Arrays.stream(activeProfiles).allMatch(p -> p.equals("qa") || p.equals("dev"));

    if (!isValid) {
      throw new IllegalStateException(
          "Only 'qa' and 'dev' profiles are allowed. Current profiles: "
              + Arrays.toString(activeProfiles));
    }
  }
}
