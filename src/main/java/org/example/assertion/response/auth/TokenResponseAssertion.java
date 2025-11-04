package org.example.assertion.response.auth;

import io.qameta.allure.Step;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.example.assertion.AssertionUtils;
import org.example.assertion.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.response.auth.Token;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TokenResponseAssertion extends ResponseAssertion<TokenResponseAssertion> {

  private final ResponseMapper responseMapper;
  private final AssertionUtils assertionUtils;
  private Token token;

  @Step("Extract token from response body")
  public TokenResponseAssertion body() {
    token = responseMapper.map(response).toTokenResponse();
    return this;
  }

  @Step("Assert that token has 15 length")
  public void tokenHas15Length() {
    int expectedLength = 15;

    Assertions.assertThat(token).isNotNull();
    Assertions.assertThat(token.token()).isNotNull();

    assertionUtils.assertEquals(token.token().length(), expectedLength);
  }
}
