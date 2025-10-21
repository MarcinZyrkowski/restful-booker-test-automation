package org.example.health;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.example.assertion.response.StringResponseAssertion;
import org.example.context.SpringTestContext;
import org.example.model.enums.service.StringResponseBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Health Check")
class HealthCheckTest extends SpringTestContext {

  @Test
  @DisplayName("Health Check")
  void verifyHealthCheck() {
    Response response = Allure.step("Perform health check", () -> bookerClient.healthCheck());

    Allure.step(
        "Verify service is healthy",
        () -> {
          StringResponseAssertion.assertThat(response)
              .statusIsCreated()
              .body()
              .isEqualTo(StringResponseBody.CREATED.getBody());
        });
  }
}
