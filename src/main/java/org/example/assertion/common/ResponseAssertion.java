package org.example.assertion.common;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
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

  private void assertStatusCode(Response response, int expectedStatusCode) {
    Assertions.assertThat(response.getStatusCode()).isEqualTo(expectedStatusCode);
  }
}
