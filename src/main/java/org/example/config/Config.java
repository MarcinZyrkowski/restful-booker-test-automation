package org.example.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ComponentScan(basePackages = {"org.example"})
public class Config {

  @Value("${base_url}")
  private String baseUrl;

}
