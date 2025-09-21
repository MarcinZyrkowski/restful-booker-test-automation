package org.example.factory.auth;

import org.example.model.dto.request.auth.UserRequest;
import org.springframework.stereotype.Component;

@Component
public class UserRequestFactory {

  // TODO extract to app properties
  public UserRequest defaultUser() {
    return UserRequest.builder()
        .username("admin")
        .password("password123")
        .build();
  }

}
