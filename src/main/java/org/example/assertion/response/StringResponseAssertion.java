package org.example.assertion.response;

import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.example.assertion.HttpAssertion;
import org.example.mapper.ResponseMapper;

public class StringResponseAssertion extends HttpAssertion<StringResponseAssertion> {

  private String responseBody;

  private StringResponseAssertion(Response response) {
    super(response);
  }

  public static StringResponseAssertion assertThat(Response response) {
    return new StringResponseAssertion(response);
  }

  public StringResponseAssertion body() {
    responseBody = ResponseMapper.map(response).toStringResponse();
    return this;
  }

  public StringResponseAssertion isEqualTo(String expected) {
    Assertions.assertThat(responseBody)
        .isEqualTo(expected);
    return this;
  }
}
