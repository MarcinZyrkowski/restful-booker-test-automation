package org.example.assertion.response;

import io.qameta.allure.Step;
import lombok.RequiredArgsConstructor;
import org.example.assertion.AssertionUtils;
import org.example.assertion.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class StringResponseAssertion extends ResponseAssertion<StringResponseAssertion> {

  private final ResponseMapper responseMapper;
  private final AssertionUtils assertionUtils;
  private String responseBody;

  @Step("Extract string from response body")
  public StringResponseAssertion body() {
    responseBody = responseMapper.map(response).toStringResponse();
    return this;
  }

  @Step("Assert that response body is equal to expected")
  public StringResponseAssertion isEqualTo(String expected) {
    assertionUtils.assertEquals(responseBody, expected);
    return this;
  }
}
