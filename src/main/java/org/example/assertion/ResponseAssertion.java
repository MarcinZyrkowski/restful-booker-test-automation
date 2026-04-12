package org.example.assertion;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ResponseAssertion<T extends ResponseAssertion<T>> {

  protected Response response;

  public void assertStatusCode(int httpStatusCode) {
    Objects.requireNonNull(response);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(httpStatusCode);
  }

  @Step("Assert that response")
  @SuppressWarnings("unchecked")
  public T assertThat(Response response) {
    this.response = response;
    return (T) this;
  }

  @Step("Status code is OK (200)")
  @SuppressWarnings("unchecked")
  public T statusIsOk() {
    assertStatusCode(HttpStatus.SC_OK);
    return (T) this;
  }
}
