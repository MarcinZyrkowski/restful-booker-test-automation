package org.example.health;

import io.restassured.response.Response;
import org.example.context.SpringTestContext;
import org.example.assertion.response.StringResponseAssertion;
import org.junit.jupiter.api.Test;

class HealthCheckTest extends SpringTestContext {

  @Test
  void verifyHealthCheck() {
    Response response = bookerClient.healthCheck();

    StringResponseAssertion.assertThat(response)
        .statusIsCreated()
        .body()
        .isEqualTo("Created");
  }

}
