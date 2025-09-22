package org.example.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ComponentScan(basePackages = {"org.example"})
public class AppConfig {

  @Value("${base_url}")
  private String baseUrl;

  @Value("${username}")
  private String username;

  @Value("${password}")
  private String password;

}
