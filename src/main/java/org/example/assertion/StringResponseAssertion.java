package org.example.assertion;

import io.restassured.response.Response;
import org.assertj.core.api.Assertions;

public class StringResponseAssertion extends HttpAssertion<StringResponseAssertion>{

  private String responseBody;

  private StringResponseAssertion(Response response) {
    super(response);
  }

  public static StringResponseAssertion assertThat(Response response) {
    return new StringResponseAssertion(response);
  }

  public StringResponseAssertion body() {
    responseBody = response.getBody().asString();
    return this;
  }

  public StringResponseAssertion isEqualTo(String expected) {
    Assertions.assertThat(responseBody)
        .isEqualTo(expected);
    return this;
  }
}
