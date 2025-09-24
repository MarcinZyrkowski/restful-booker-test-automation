package org.example.factory.auth;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.config.AppConfiguration;
import org.example.model.dto.request.auth.UserRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRequestFactory {

  public static UserRequest defaultUser() {
    return UserRequest.builder()
        .username(AppConfiguration.CONFIG.username())
        .password(AppConfiguration.CONFIG.password())
        .build();
  }

}
