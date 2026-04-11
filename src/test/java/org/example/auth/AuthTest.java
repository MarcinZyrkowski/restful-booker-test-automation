package org.example.auth;

import io.restassured.response.Response;
import org.example.SpringTestContext;
import org.example.model.dto.request.auth.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.example.utils.FakerUtils.FAKER;

@DisplayName("Auth")
class AuthTest extends SpringTestContext {

  @Test
  @DisplayName("Create token with valid user")
  void createTokenTest() {
    Response response = bookerClient.createToken(adminUser);

    tokenResponseAssertion.assertThat(response).statusIsOk().body().tokenHas15Length();
  }

  @Test
  @DisplayName("Token should not be create for invalid user")
  void createTokenWithInvalidUserTest() {
    User invalidUser =
        User.builder()
            .username(FAKER.name().username())
            .password(FAKER.internet().password())
            .build();

    Response response = bookerClient.createToken(invalidUser);

    errorResponseAssertion.assertThat(response).statusIsOk().body().reasonIs("Bad credentials");
  }
}
