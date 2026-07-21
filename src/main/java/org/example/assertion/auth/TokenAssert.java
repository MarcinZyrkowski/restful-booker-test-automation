package org.example.assertion.auth;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.example.model.service.dto.response.auth.Token;

public class TokenAssert extends AbstractAssert<TokenAssert, Token> {

  protected TokenAssert(Token actual) {
    super(actual, TokenAssert.class);
  }

  public static TokenAssert assertThat(Token actual) {
    return new TokenAssert(actual);
  }

  public TokenAssert hasLengthOf(int expectedLength) {
    isNotNull();
    Assertions.assertThat(actual.token()).hasSize(expectedLength);
    return this;
  }
}
