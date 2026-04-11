package org.example.assertionNG;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.springframework.stereotype.Component;

@Component
public class ResponseAssertion {

  private void assertStatusCode(Response response, int expectedStatusCode) {
    Assertions.assertThat(response.getStatusCode()).isEqualTo(expectedStatusCode);
  }

  protected void assertStatusCodeIsCreated(Response response) {
    assertStatusCode(response, HttpStatus.SC_CREATED);
  }

  protected void assertStatusCodeIsOk(Response response) {
    assertStatusCode(response, HttpStatus.SC_OK);
  }

  protected void assertStatusCodeIsForbidden(Response response) {
    assertStatusCode(response, HttpStatus.SC_FORBIDDEN);
  }

  protected void assertStatusCodeIsNotFound(Response response) {
    assertStatusCode(response, HttpStatus.SC_NOT_FOUND);
  }

  protected void assertStatusCodeIsMethodNotAllowed(Response response) {
    assertStatusCode(response, HttpStatus.SC_METHOD_NOT_ALLOWED);
  }

  protected void assertStatusCodeIsInternalServerError(Response response) {
    assertStatusCode(response, HttpStatus.SC_INTERNAL_SERVER_ERROR);
  }
}
