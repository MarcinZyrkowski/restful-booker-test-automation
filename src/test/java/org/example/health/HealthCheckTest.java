package org.example.health;

import io.restassured.response.Response;
import org.example.SpringTestContext;
import org.example.model.enums.service.StringResponseBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Health Check")
class HealthCheckTest extends SpringTestContext {

  @Test
  @DisplayName("Health Check")
  void verifyHealthCheck() {
    Response response = bookerClient.healthCheck();

    stringResponseAssertion
        .assertThat(response)
        .statusIsCreated()
        .body()
        .isEqualTo(StringResponseBody.CREATED.getBody());
  }
}
