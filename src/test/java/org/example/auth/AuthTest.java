package org.example.auth;

import io.restassured.response.Response;
import org.example.context.SpringTestContext;
import org.example.assertion.response.auth.TokenResponseAssertion;
import org.example.factory.auth.UserRequestFactory;
import org.example.model.dto.request.auth.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Auth")
class AuthTest extends SpringTestContext {

  @Test
  @DisplayName("Create token with valid user")
  void createTokenTest() {
    User user = UserRequestFactory.defaultUser();

    Response response = bookerClient.createToken(user);

    TokenResponseAssertion.assertThat(response)
        .statusIsOk()
        .body()
        .tokenHas15Length();
  }

}
