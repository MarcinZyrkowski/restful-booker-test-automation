package org.example.auth;

import io.restassured.response.Response;
import org.example.SpringTestContext;
import org.example.model.dto.request.auth.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Auth")
class AuthTest extends SpringTestContext {

  @Test
  @DisplayName("Create token with valid user")
  void createTokenTest() {
    Response response = bookerClient.createToken(adminUser);

    tokenResponseAssertionNG.assertTokenHas15Length(response);
  }

  @Test
  @DisplayName("Token should not be create for invalid user")
  void createTokenWithInvalidUserTest() {
    User invalidUser = userFactory.getWithInvalidCredentials();

    Response response = bookerClient.createToken(invalidUser);

    errorResponseAssertion.assertThat(response).statusIsOk().body().reasonIsBadCredentials();
  }
}
