package org.example.api;

import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import org.example.config.SpringConfig;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthRequestDecorator {

  private final SpringConfig springConfig;

  /**
   * Applies preemptive basic authentication using configured system credentials.
   *
   * @param spec The request specification to decorate.
   * @return The decorated request specification with basic auth.
   */
  public RequestSpecification withBasicAuth(RequestSpecification spec) {
    return spec.auth().preemptive().basic(springConfig.getUsername(), springConfig.getPassword());
  }

  /**
   * Applies token-based cookie authentication.
   *
   * @param spec The request specification to decorate.
   * @param token The auth token string.
   * @return The decorated request specification with token cookie.
   */
  public RequestSpecification withTokenAuth(RequestSpecification spec, String token) {
    return spec.cookie("token", token);
  }
}
