package org.example.health;

import org.example.assertion.common.StringResponseAssert;
import org.example.client.health.HealthClient;
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

  @Autowired private HealthClient healthClient;

  @Test
  @DisplayName("Verify Health Check")
  void verifyHealthCheck() {
    String response = healthClient.healthCheck();

    StringResponseAssert.assertThat(response).isCreated();
  }
}
