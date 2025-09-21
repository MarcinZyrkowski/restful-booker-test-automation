package org.example.auth;

import io.restassured.response.Response;
import org.example.context.SpringTestContext;
import org.example.assertion.response.auth.TokenResponseAssertion;
import org.example.model.dto.request.auth.UserRequest;
import org.junit.jupiter.api.Test;

class AuthTest extends SpringTestContext {

  @Test
  void createTokenTest() {
    UserRequest userRequest = userRequestFactory.defaultUser();

    Response response = bookerClient.createToken(userRequest);

    TokenResponseAssertion.assertThat(response)
        .statusIsOk()
        .body()
        .tokenHas15Length();
  }

}
