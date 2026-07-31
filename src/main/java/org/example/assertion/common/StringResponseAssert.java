package org.example.assertion.common;

import io.qameta.allure.Step;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.example.model.service.enums.HttpStatusText;

public class StringResponseAssert extends AbstractAssert<StringResponseAssert, String> {

  private static final String EXPECTED_RESPONSE_TEXT_MESSAGE =
      "Expected response text to be %s but was %s";

  protected StringResponseAssert(String actual) {
    super(actual, StringResponseAssert.class);
  }

  public static StringResponseAssert assertThat(String actual) {
    return new StringResponseAssert(actual);
  }

  private void hasResponseMessage(HttpStatusText statusText) {
    isNotNull();
    Assertions.assertThat(actual)
        .withFailMessage(EXPECTED_RESPONSE_TEXT_MESSAGE, statusText.getBody(), actual)
        .isEqualTo(statusText.getBody());
  }

  @Step("Verify response text is Created")
  public StringResponseAssert hasCreatedMessage() {
    hasResponseMessage(HttpStatusText.CREATED);
    return this;
  }

  @Step("Verify response text is Bad Request")
  public StringResponseAssert hasBadRequestMessage() {
    hasResponseMessage(HttpStatusText.BAD_REQUEST);
    return this;
  }

  @Step("Verify response text is Forbidden")
  public StringResponseAssert hasForbiddenMessage() {
    hasResponseMessage(HttpStatusText.FORBIDDEN);
    return this;
  }

  @Step("Verify response text is Not Found")
  public StringResponseAssert hasNotFoundMessage() {
    hasResponseMessage(HttpStatusText.NOT_FOUND);
    return this;
  }

  @Step("Verify response text is Internal Server Error")
  public StringResponseAssert hasInternalServerErrorMessage() {
    hasResponseMessage(HttpStatusText.INTERNAL_SERVER_ERROR);
    return this;
  }

  @Step("Verify response text is Method Not Allowed")
  public StringResponseAssert hasMethodNotAllowedMessage() {
    hasResponseMessage(HttpStatusText.METHOD_NOT_ALLOWED);
    return this;
  }
}
