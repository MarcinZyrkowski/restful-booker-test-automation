package org.example.assertion.response.auth;

import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.example.assertion.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.response.auth.Token;

public class TokenResponseAssertion extends ResponseAssertion<TokenResponseAssertion> {

  private Token token;

  private TokenResponseAssertion(Response response) {
    super(response);
  }

  public static TokenResponseAssertion assertThat(Response response) {
    return new TokenResponseAssertion(response);
  }

  public TokenResponseAssertion body() {
    token = ResponseMapper.map(response).toTokenResponse();
    return this;
  }

  public void tokenHas15Length() {
    int expectedLength = 15;

    Assertions.assertThat(token).isNotNull();
    Assertions.assertThat(token.token()).isNotNull();

    Assertions.assertThat(token.token().length()).isEqualTo(expectedLength);
  }
}
