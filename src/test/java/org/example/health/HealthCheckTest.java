package org.example.health;

import io.restassured.response.Response;
import org.example.SpringContext;
import org.example.assertion.response.StringResponseAssertion;
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
