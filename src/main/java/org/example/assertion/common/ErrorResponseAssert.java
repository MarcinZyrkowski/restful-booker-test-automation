package org.example.assertion.common;

import io.qameta.allure.Step;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.example.model.service.dto.response.common.ErrorResponse;

public class ErrorResponseAssert extends AbstractAssert<ErrorResponseAssert, ErrorResponse> {

  protected ErrorResponseAssert(ErrorResponse actual) {
    super(actual, ErrorResponseAssert.class);
  }

  public static ErrorResponseAssert assertThat(ErrorResponse actual) {
    return new ErrorResponseAssert(actual);
  }

  @Step("Verify error response reason is '{expectedReason}'")
  public ErrorResponseAssert hasReason(String expectedReason) {
    isNotNull();
    Assertions.assertThat(actual.reason())
        .withFailMessage("Expected reason to be '%s' but was '%s'", expectedReason, actual.reason())
        .isEqualTo(expectedReason);
    return this;
  }
}
