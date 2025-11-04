package org.example.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import org.example.config.SpringConfig;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestClient {

  protected final SpringConfig springConfig;

  public RequestSpecification basicRequest() {
    return RestAssured.given()
        .contentType(ContentType.JSON)
        .baseUri(springConfig.getBaseUrl())
        .filter(new AllureRestAssured())
        .filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
  }
}
