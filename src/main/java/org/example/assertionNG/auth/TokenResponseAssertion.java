package org.example.assertionNG.auth;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.example.assertionNG.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.response.auth.Token;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenResponseAssertion extends ResponseAssertion {

  private final ResponseMapper responseMapper;

  @Step("Assert that token has 15 length")
  public void assertTokenHas15Length(Response response) {
    assertStatusCodeIsOk(response);

    Token token = extractTokenResponse(response);
    assertValidToken(token);
  }

  private void assertValidToken(Token token) {
    Assertions.assertThat(token).isNotNull();
    Assertions.assertThat(token.token()).isNotNull();
    int expectedLength = 15;
    Assertions.assertThat(token.token().length()).isEqualTo(expectedLength);
  }

  private Token extractTokenResponse(Response response) {
    return responseMapper.map(response).toTokenResponse();
  }
}
