package org.example.auth;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.assertion.auth.TokenAssert;
import org.example.assertion.common.ErrorResponseAssert;
import org.example.client.token.TokenClient;
import org.example.config.SpringConfig;
import org.example.factory.auth.UserFactory;
import org.example.model.service.dto.request.auth.User;
import org.example.model.service.dto.response.auth.Token;
import org.example.model.service.dto.response.common.ErrorResponse;
import org.example.tags.Regression;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Severity(SeverityLevel.BLOCKER)
@Epic("Security & Identity")
@Feature("Authentication token")
@Story("Authentication")
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
    // Attempt to create a token for the valid admin user
    Token token = tokenClient.createToken(adminUser);

    // Verify the returned token length
    TokenAssert.assertThat(token).hasLengthOf(15);
  }

  @Test
  @DisplayName("Token should not be created for invalid user")
  void createTokenWithInvalidUserTest() {
    // Generate user object with invalid credentials
    User invalidUser = userFactory.getWithInvalidCredentials();

    // Attempt to create a token and expect an error response
    ErrorResponse errorResponse = tokenClient.createTokenExpectingError(invalidUser);

    // Verify the response contains the 'Bad credentials' error message
    ErrorResponseAssert.assertThat(errorResponse).hasReason("Bad credentials");
  }
}
