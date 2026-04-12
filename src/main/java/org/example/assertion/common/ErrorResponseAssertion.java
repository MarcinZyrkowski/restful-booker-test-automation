package org.example.assertion.common;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.response.auth.ErrorResponse;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class ErrorResponseAssertion extends ResponseAssertion {

  private final ResponseMapper responseMapper;

  @Step("Assert that error reason is Bad credentials")
  public void assertReasonIsBadCredentials(Response response) {
    assertStatusCodeIsOk(response);
    ErrorResponse errorResponse = extractErrorResponse(response);

    assertBadCredentials(errorResponse);
  }

  private void assertBadCredentials(ErrorResponse errorResponse) {
    String expectedReason = "Bad credentials";
    assertReasonMessage(errorResponse, expectedReason);
  }

  private void assertReasonMessage(ErrorResponse errorResponse, String expectedReason) {
    Assertions.assertThat(errorResponse).isNotNull();
    Assertions.assertThat(errorResponse.reason()).isNotNull();
    Assertions.assertThat(errorResponse.reason()).isEqualTo(expectedReason);
  }

  private ErrorResponse extractErrorResponse(Response response) {
    return responseMapper.map(response).toErrorResponse();
  }
}
