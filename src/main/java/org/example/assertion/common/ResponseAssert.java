package org.example.assertion.common;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class ResponseAssert extends AbstractAssert<ResponseAssert, Response> {

  protected ResponseAssert(Response actual) {
    super(actual, ResponseAssert.class);
  }

  public static ResponseAssert assertThat(Response actual) {
    return new ResponseAssert(actual);
  }

  @Step("Verify response status is 201 Created")
  public ResponseAssert isCreated() {
    isNotNull();
    assertStatusCode(201);
    return this;
  }

  @Step("Verify response status is 200 OK")
  public ResponseAssert isOk() {
    isNotNull();
    assertStatusCode(200);
    return this;
  }

  @Step("Verify response status is 403 Forbidden")
  public ResponseAssert isForbidden() {
    isNotNull();
    assertStatusCode(403);
    return this;
  }

  @Step("Verify response status is 404 Not Found")
  public ResponseAssert isNotFound() {
    isNotNull();
    assertStatusCode(404);
    return this;
  }

  @Step("Verify response status is 405 Method Not Allowed")
  public ResponseAssert isMethodNotAllowed() {
    isNotNull();
    assertStatusCode(405);
    return this;
  }

  @Step("Verify response status is Bad Request or Internal Server Error")
  public ResponseAssert isBadRequestOrInternalServerError() {
    isNotNull();
    Assertions.assertThat(actual.getStatusCode())
        .withFailMessage(
            "Expected status code to be Bad Request or Internal Server Error but was %d",
            actual.getStatusCode())
        .isIn(400, 500);
    return this;
  }

  private void assertStatusCode(int expectedStatusCode) {
    Assertions.assertThat(actual.getStatusCode())
        .withFailMessage(
            "Expected status code to be %d but was %d", expectedStatusCode, actual.getStatusCode())
        .isEqualTo(expectedStatusCode);
  }
}
