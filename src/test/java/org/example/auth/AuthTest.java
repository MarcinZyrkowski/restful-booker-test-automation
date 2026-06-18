package org.example.auth;

import io.restassured.response.Response;
import org.example.assertion.auth.TokenResponseAssertion;
import org.example.assertion.common.ErrorResponseAssertion;
import org.example.client.BookerClient;
import org.example.config.SpringConfig;
import org.example.factory.auth.UserFactory;
import org.example.model.service.dto.request.auth.User;
import org.example.tags.Regression;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Regression
@SpringBootTest(classes = SpringConfig.class)
@DisplayName("Auth")
class AuthTest {

  @Autowired private BookerClient bookerClient;
  @Autowired private User adminUser;
  @Autowired private TokenResponseAssertion tokenResponseAssertion;
  @Autowired private UserFactory userFactory;
  @Autowired private ErrorResponseAssertion errorResponseAssertion;

  @Test
  @DisplayName("Create token with valid user")
  void createTokenTest() {
    Response response = bookerClient.createToken(adminUser);

    tokenResponseAssertion.assertTokenHas15Length(response);
  }

  @Test
  @DisplayName("Token should not be create for invalid user")
  void createTokenWithInvalidUserTest() {
    User invalidUser = userFactory.getWithInvalidCredentials();

    Response response = bookerClient.createToken(invalidUser);

    errorResponseAssertion.assertReasonIsBadCredentials(response);
  }
}
