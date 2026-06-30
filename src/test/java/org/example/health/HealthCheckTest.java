package org.example.health;

import io.restassured.response.Response;
import org.example.assertion.common.StringResponseAssertion;
import org.example.client.BookerClient;
import org.example.config.SpringConfig;
import org.example.tags.Regression;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Regression
@SpringBootTest(classes = SpringConfig.class)
@DisplayName("Health Check")
class HealthCheckTest {

  @Autowired private BookerClient bookerClient;
  @Autowired private StringResponseAssertion stringResponseAssertion;

  @Test
  @DisplayName("Health Check")
  void verifyHealthCheck() {
    Response response = bookerClient.healthCheck();

    stringResponseAssertion.assertResponseIsCreated(response);
  }
}
