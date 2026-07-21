package org.example.assertion.common;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.example.model.service.dto.response.auth.ErrorResponse;

public class ErrorResponseAssert extends AbstractAssert<ErrorResponseAssert, ErrorResponse> {

  protected ErrorResponseAssert(ErrorResponse actual) {
    super(actual, ErrorResponseAssert.class);
  }

  public static ErrorResponseAssert assertThat(ErrorResponse actual) {
    return new ErrorResponseAssert(actual);
  }

  public ErrorResponseAssert hasReason(String expectedReason) {
    isNotNull();
    Assertions.assertThat(actual.reason()).isEqualTo(expectedReason);
    return this;
  }
}
