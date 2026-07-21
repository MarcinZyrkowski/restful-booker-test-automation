package org.example.auth;

import org.example.assertion.auth.TokenAssert;
import org.example.assertion.common.ErrorResponseAssert;
import org.example.client.token.TokenClient;
import org.example.config.SpringConfig;
import org.example.factory.auth.UserFactory;
import org.example.model.service.dto.request.auth.User;
import org.example.model.service.dto.response.auth.ErrorResponse;
import org.example.model.service.dto.response.auth.Token;
import org.example.tags.Regression;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Regression
@SpringBootTest(classes = SpringConfig.class)
@DisplayName("Auth")
class AuthTest {

  @Autowired private TokenClient tokenClient;
  @Autowired private User adminUser;
  @Autowired private UserFactory userFactory;

  @Test
  @DisplayName("Create token with valid user")
  void createTokenTest() {
    Token token = tokenClient.createToken(adminUser);

    TokenAssert.assertThat(token).hasLengthOf(15);
  }

  @Test
  @DisplayName("Token should not be create for invalid user")
  void createTokenWithInvalidUserTest() {
    User invalidUser = userFactory.getWithInvalidCredentials();

    ErrorResponse errorResponse = tokenClient.createTokenExpectingError(invalidUser);

    ErrorResponseAssert.assertThat(errorResponse).hasReason("Bad credentials");
  }
}
