package org.example.factory.auth;

import io.qameta.allure.Step;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.config.AppConfiguration;
import org.example.model.dto.request.auth.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRequestFactory {

  @Step("Prepare default user")
  public static User defaultUser() {
    return User.builder()
        .username(AppConfiguration.CONFIG.username())
        .password(AppConfiguration.CONFIG.password())
        .build();
  }
}
