package org.example.assertion.common;

import io.qameta.allure.Step;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.example.model.service.enums.HttpStatusText;

public class StringResponseAssert extends AbstractAssert<StringResponseAssert, String> {

  protected StringResponseAssert(String actual) {
    super(actual, StringResponseAssert.class);
  }

  public static StringResponseAssert assertThat(String actual) {
    return new StringResponseAssert(actual);
  }

  @Step("Verify response text is Created")
  public StringResponseAssert hasCreatedMessage() {
    isNotNull();
    Assertions.assertThat(actual)
        .withFailMessage(
            "Expected response text to be %s but was %s", HttpStatusText.CREATED.getBody(), actual)
        .isEqualTo(HttpStatusText.CREATED.getBody());
    return this;
  }

  @Step("Verify response text is Bad Request")
  public StringResponseAssert hasBadRequestMessage() {
    isNotNull();
    Assertions.assertThat(actual)
        .withFailMessage(
            "Expected response text to be %s but was %s",
            HttpStatusText.BAD_REQUEST.getBody(), actual)
        .isEqualTo(HttpStatusText.BAD_REQUEST.getBody());
    return this;
  }

  @Step("Verify response text is Forbidden")
  public StringResponseAssert hasForbiddenMessage() {
    isNotNull();
    Assertions.assertThat(actual)
        .withFailMessage(
            "Expected response text to be %s but was %s",
            HttpStatusText.FORBIDDEN.getBody(), actual)
        .isEqualTo(HttpStatusText.FORBIDDEN.getBody());
    return this;
  }

  @Step("Verify response text is Not Found")
  public StringResponseAssert hasNotFoundMessage() {
    isNotNull();
    Assertions.assertThat(actual)
        .withFailMessage(
            "Expected response text to be %s but was %s",
            HttpStatusText.NOT_FOUND.getBody(), actual)
        .isEqualTo(HttpStatusText.NOT_FOUND.getBody());
    return this;
  }

  @Step("Verify response text is Internal Server Error")
  public StringResponseAssert hasInternalServerErrorMessage() {
    isNotNull();
    Assertions.assertThat(actual)
        .withFailMessage(
            "Expected response text to be %s but was %s",
            HttpStatusText.INTERNAL_SERVER_ERROR.getBody(), actual)
        .isEqualTo(HttpStatusText.INTERNAL_SERVER_ERROR.getBody());
    return this;
  }

  @Step("Verify response text is Method Not Allowed")
  public StringResponseAssert hasMethodNotAllowedMessage() {
    isNotNull();
    Assertions.assertThat(actual)
        .withFailMessage(
            "Expected response text to be %s but was %s",
            HttpStatusText.METHOD_NOT_ALLOWED.getBody(), actual)
        .isEqualTo(HttpStatusText.METHOD_NOT_ALLOWED.getBody());
    return this;
  }
}
