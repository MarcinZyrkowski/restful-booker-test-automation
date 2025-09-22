package org.example.factory.auth;

import lombok.RequiredArgsConstructor;
import org.example.config.AppConfig;
import org.example.model.dto.request.auth.UserRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRequestFactory {

  private final AppConfig appConfig;

  public UserRequest defaultUser() {
    return UserRequest.builder()
        .username(appConfig.getUsername())
        .password(appConfig.getPassword())
        .build();
  }

}
