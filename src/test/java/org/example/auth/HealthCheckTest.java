package org.example.auth;

import io.restassured.response.Response;
import org.example.SpringContext;
import org.example.assertion.StringResponseAssertion;
import org.junit.jupiter.api.Test;

class HealthCheckTest extends SpringContext {

  @Test
  void verifyHealthCheck() {
    Response response = bookerClient.healthCheck();

    StringResponseAssertion.assertThat(response)
        .statusIsCreated()
        .body()
        .isEqualTo("Created");
  }

}
