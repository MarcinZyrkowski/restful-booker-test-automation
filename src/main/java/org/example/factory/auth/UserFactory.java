package org.example.factory.auth;

import io.qameta.allure.Step;
import org.example.generator.request.UserGenerator;
import org.example.model.dto.request.auth.User;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

  @Step("Prepare user with invalid credentials")
  public User getWithInvalidCredentials() {
    return UserGenerator.builder().withInvalidCredentials().build();
  }
}
