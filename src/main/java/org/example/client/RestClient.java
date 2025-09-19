package org.example.client;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import org.example.config.AppConfig;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RestClient {

  private final AppConfig appConfig;

  public RequestSpecification basicRequest() {
    return RestAssured.given()
        .contentType(ContentType.JSON)
        .baseUri(appConfig.getBaseUrl())
        .filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
  }

}
