package org.example.assertionNG;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.example.mapper.ResponseMapper;
import org.example.model.enums.service.StringResponseBody;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StringResponseAssertion extends ResponseAssertion {

  private final ResponseMapper responseMapper;

  @Step("Assert that response is created")
  public void assertResponseIsCreated(Response response) {
    assertStatusCodeIsCreated(response);
    String stringResponse = extractStringResponse(response);
    assertEquals(stringResponse, StringResponseBody.CREATED.getBody());
  }

  private String extractStringResponse(Response response) {
    return responseMapper.map(response).toStringResponse();
  }

  private void assertEquals(String actual, String expected) {
    Assertions.assertThat(actual).isEqualTo(expected);
  }
}
