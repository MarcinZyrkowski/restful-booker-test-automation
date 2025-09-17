package org.example.factory;

import org.example.model.request.UserRequest;
import org.springframework.stereotype.Component;

@Component
public class UserRequestFactory {

  public UserRequest defaultUser() {
    return UserRequest.builder()
        .username("admin")
        .password("password123")
        .build();
  }

}
