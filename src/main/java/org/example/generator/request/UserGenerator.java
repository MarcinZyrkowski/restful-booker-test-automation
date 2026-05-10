package org.example.generator.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.model.dto.request.auth.User;
import org.example.utils.BookerStringUtils;
import org.example.utils.FakerUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserGenerator {

  private User user;

  public static UserGenerator builder() {
    return new UserGenerator();
  }

  public User build() {
    return user;
  }

  public UserGenerator withInvalidCredentials() {
    this.user =
        User.builder()
            .username(BookerStringUtils.randomFullName())
            .password(FakerUtils.generatePassword())
            .build();
    return this;
  }
}
