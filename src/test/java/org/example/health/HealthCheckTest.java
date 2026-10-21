package org.example.health;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.assertion.common.StringResponseAssert;
import org.example.client.health.HealthClient;
import org.example.config.SpringConfig;
import org.example.tags.Regression;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Severity(SeverityLevel.BLOCKER)
@Epic("System Infrastructure")
@Feature("API Health")
@Story("Health Check")
@Regression
@SpringBootTest(classes = SpringConfig.class)
@DisplayName("Health Check")
class HealthCheckTest {

  @Autowired private HealthClient healthClient;

  @Test
  @DisplayName("Verify Health Check")
  void verifyHealthCheck() {
    // Send health check ping request
    String response = healthClient.healthCheck();

    // Verify a created status code is returned
    StringResponseAssert.assertThat(response).hasCreatedMessage();
  }
}
