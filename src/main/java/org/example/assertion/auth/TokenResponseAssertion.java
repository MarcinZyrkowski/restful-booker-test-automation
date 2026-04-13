package org.example.assertion.auth;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.example.assertion.common.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.response.auth.Token;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TokenResponseAssertion {

  private final ResponseMapper responseMapper;
  private final ResponseAssertion responseAssertion;

  @Step("Assert that token has 15 length")
  public void assertTokenHas15Length(Response response) {
    responseAssertion.assertStatusCodeIsOk(response);

    Token token = responseMapper.mapToTokenResponse(response);
    assertValidToken(token);
  }

  private void assertValidToken(Token token) {
    Assertions.assertThat(token).isNotNull();
    Assertions.assertThat(token.token()).isNotNull();
    int expectedLength = 15;
    Assertions.assertThat(token.token().length()).isEqualTo(expectedLength);
  }
}
