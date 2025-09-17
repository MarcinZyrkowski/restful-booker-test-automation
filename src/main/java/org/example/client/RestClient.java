package org.example.client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import org.example.config.Config;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RestClient {

  private final Config config;

  public RequestSpecification basicRequest() {
    return RestAssured.given()
        .contentType(ContentType.JSON)
        .baseUri(config.getBaseUrl());
  }

}
