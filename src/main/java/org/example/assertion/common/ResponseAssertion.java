package org.example.assertion.common;

import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResponseAssertion {

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

  public void assertStatusCodeIsBadRequest(Response response) {
    assertStatusCode(response, HttpStatus.SC_BAD_REQUEST);
  }

  private void assertStatusCode(Response response, int expectedStatusCode) {
    Assertions.assertThat(response.getStatusCode())
        .withFailMessage(
            "Expected status code to be %d but was %d",
            expectedStatusCode, response.getStatusCode())
        .isEqualTo(expectedStatusCode);
  }
}
