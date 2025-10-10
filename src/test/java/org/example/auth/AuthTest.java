package org.example.auth;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.example.assertion.response.auth.TokenResponseAssertion;
import org.example.context.SpringTestContext;
import org.example.factory.auth.UserRequestFactory;
import org.example.model.dto.request.auth.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Auth")
class AuthTest extends SpringTestContext {

  @Test
  @DisplayName("Create token with valid user")
  void createTokenTest() {
    User user = Allure.step("Prepare user with valid credentials",
        UserRequestFactory::defaultUser);

    Response response = Allure.step("Send create token request",
        () -> bookerClient.createToken(user));

    Allure.step("Verify token is created successfully", () ->
        TokenResponseAssertion.assertThat(response)
            .statusIsOk()
            .body()
            .tokenHas15Length());
  }

}
