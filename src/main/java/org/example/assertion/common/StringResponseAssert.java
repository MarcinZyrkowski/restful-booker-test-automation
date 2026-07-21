package org.example.assertion.common;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class StringResponseAssert extends AbstractAssert<StringResponseAssert, String> {

  protected StringResponseAssert(String actual) {
    super(actual, StringResponseAssert.class);
  }

  public static StringResponseAssert assertThat(String actual) {
    return new StringResponseAssert(actual);
  }

  public StringResponseAssert isCreated() {
    isNotNull();
    Assertions.assertThat(actual).isEqualTo("Created");
    return this;
  }

  public StringResponseAssert isBadRequest() {
    isNotNull();
    Assertions.assertThat(actual).isEqualTo("Bad Request");
    return this;
  }

  public StringResponseAssert isForbidden() {
    isNotNull();
    Assertions.assertThat(actual).isEqualTo("Forbidden");
    return this;
  }

  public StringResponseAssert isNotFound() {
    isNotNull();
    Assertions.assertThat(actual).isEqualTo("Not Found");
    return this;
  }

  public StringResponseAssert isInternalServerError() {
    isNotNull();
    Assertions.assertThat(actual).isEqualTo("Internal Server Error");
    return this;
  }

  public StringResponseAssert isMethodNotAllowed() {
    isNotNull();
    Assertions.assertThat(actual).isEqualTo("Method Not Allowed");
    return this;
  }
}
