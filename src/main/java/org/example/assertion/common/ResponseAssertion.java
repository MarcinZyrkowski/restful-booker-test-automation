package org.example.assertion.common;

import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResponseAssertion {

  private final AssertionUtils assertionUtils;

  public void assertStatusCodeIsCreated(Response response) {
    assertStatusCode(response, HttpStatus.SC_CREATED);
  }

  public void assertStatusCodeIsOk(Response response) {
    assertStatusCode(response, HttpStatus.SC_OK);
  }

  public void assertStatusCodeIsForbidden(Response response) {
    assertStatusCode(response, HttpStatus.SC_FORBIDDEN);
  }

  public void assertStatusCodeIsNotFound(Response response) {
    assertStatusCode(response, HttpStatus.SC_NOT_FOUND);
  }

  public void assertStatusCodeIsMethodNotAllowed(Response response) {
    assertStatusCode(response, HttpStatus.SC_METHOD_NOT_ALLOWED);
  }

  public void assertStatusCodeIsInternalServerError(Response response) {
    assertStatusCode(response, HttpStatus.SC_INTERNAL_SERVER_ERROR);
  }

  private void assertStatusCode(Response response, int expectedStatusCode) {
    assertionUtils.assertEquals(response.getStatusCode(), expectedStatusCode);
  }
}
