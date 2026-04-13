package org.example.assertion.common;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.response.auth.ErrorResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ErrorResponseAssertion {

  private final ResponseMapper responseMapper;
  private final ResponseAssertion responseAssertion;

  @Step("Assert that error reason is Bad credentials")
  public void assertReasonIsBadCredentials(Response response) {
    responseAssertion.assertStatusCodeIsOk(response);
    ErrorResponse errorResponse = responseMapper.mapToErrorResponse(response);

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
}
