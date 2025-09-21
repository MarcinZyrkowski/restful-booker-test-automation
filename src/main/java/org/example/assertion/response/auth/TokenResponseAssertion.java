package org.example.assertion.response.auth;

import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.example.assertion.HttpAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.response.auth.TokenResponse;

public class TokenResponseAssertion extends HttpAssertion<TokenResponseAssertion> {

  private TokenResponse tokenResponse;

  private TokenResponseAssertion(Response response) {
    super(response);
  }

  public static TokenResponseAssertion assertThat(Response response) {
    return new TokenResponseAssertion(response);
  }

  public TokenResponseAssertion body() {
    tokenResponse = ResponseMapper.map(response).toTokenResponse();
    return this;
  }

  public void tokenHas15Length() {
    int expectedLength = 15;

    Assertions.assertThat(tokenResponse).isNotNull();
    Assertions.assertThat(tokenResponse.token()).isNotNull();

    Assertions.assertThat(tokenResponse.token().length()).isEqualTo(expectedLength);
  }

}
