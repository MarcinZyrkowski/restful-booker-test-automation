package org.example.assertion.response;

import io.qameta.allure.Step;
import lombok.RequiredArgsConstructor;
import org.example.assertion.AssertionUtils;
import org.example.assertion.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.response.auth.ErrorResponse;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class ErrorResponseAssertion extends ResponseAssertion<ErrorResponseAssertion> {

  private final ResponseMapper responseMapper;
  private final AssertionUtils assertionUtils;
  private ErrorResponse errorResponse;

  @Step("Extract error from response body")
  public ErrorResponseAssertion body() {
    errorResponse = responseMapper.map(response).toErrorResponse();
    return this;
  }

  @Step("Assert that error reason is {reason}")
  public void reasonIs(String reason) {
    assertionUtils.assertEquals(errorResponse.reason(), reason);
  }
}
