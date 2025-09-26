package org.example.assertion;

import io.restassured.response.Response;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;

@RequiredArgsConstructor
public abstract class HttpAssertion<T extends HttpAssertion<T>> {

  protected final Response response;

  public void assertStatusCode(int httpStatusCode) {
    Objects.requireNonNull(response);

    Assertions.assertThat(response.getStatusCode())
        .isEqualTo(httpStatusCode);
  }

  @SuppressWarnings("unchecked")
  public T statusIsOk() {
    assertStatusCode(HttpStatus.SC_OK);
    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T statusIsCreated() {
    assertStatusCode(HttpStatus.SC_CREATED);
    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T statusIsNotFound() {
    assertStatusCode(HttpStatus.SC_NOT_FOUND);
    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T statusIsInternalServerError() {
    assertStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    return (T) this;
  }

}
