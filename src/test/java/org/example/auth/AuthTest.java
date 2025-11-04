package org.example.auth;

import io.restassured.response.Response;
import org.example.SpringTestContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Auth")
class AuthTest extends SpringTestContext {

  @Test
  @DisplayName("Create token with valid user")
  void createTokenTest() {
    Response response = bookerClient.createToken(adminUser);

    tokenResponseAssertion.assertThat(response).statusIsOk().body().tokenHas15Length();
  }
}
