package org.example.config;

import lombok.Getter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ComponentScan(basePackages = {"org.example"})
public class SpringConfig {

}
