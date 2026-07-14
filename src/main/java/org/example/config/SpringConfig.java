package org.example.config;

import lombok.Getter;
import org.example.model.service.dto.request.auth.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ComponentScan(basePackages = {"org.example"})
public class SpringConfig {

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
}
