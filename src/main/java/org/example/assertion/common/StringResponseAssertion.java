package org.example.assertion.common;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.example.mapper.ResponseMapper;
import org.example.model.enums.service.StringResponseBody;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class StringResponseAssertion extends ResponseAssertion {

  private final ResponseMapper responseMapper;
  private final AssertionUtils assertionUtils;

  @Step("Assert that response is created")
  public void assertResponseIsCreated(Response response) {
    assertStatusCodeIsCreated(response);
    String stringResponse = responseMapper.mapToStringResponse(response);
    assertionUtils.assertEquals(stringResponse, StringResponseBody.CREATED.getBody());
  }

  @Step("Assert that response is not found")
  public void assertResponseIsNotFound(Response response) {
    assertStatusCodeIsNotFound(response);
    String stringResponse = responseMapper.mapToStringResponse(response);
    assertionUtils.assertEquals(stringResponse, StringResponseBody.NOT_FOUND.getBody());
  }

  @Step("Assert that response is forbidden")
  public void assertResponseIsForbidden(Response response) {
    assertStatusCodeIsForbidden(response);
    String stringResponse = responseMapper.mapToStringResponse(response);
    assertionUtils.assertEquals(stringResponse, StringResponseBody.FORBIDDEN.getBody());
  }

  @Step("Assert that response is method not allowed")
  public void assertResponseIsMethodNotAllowed(Response response) {
    assertStatusCodeIsMethodNotAllowed(response);
    String stringResponse = responseMapper.mapToStringResponse(response);
    assertionUtils.assertEquals(stringResponse, StringResponseBody.METHOD_NOT_ALLOWED.getBody());
  }

  @Step("Assert that response is internal server error")
  public void assertResponseIsInternalServerError(Response response) {
    assertStatusCodeIsInternalServerError(response);
    String stringResponse = responseMapper.mapToStringResponse(response);
    assertionUtils.assertEquals(stringResponse, StringResponseBody.INTERNAL_SERVER_ERROR.getBody());
  }
}
