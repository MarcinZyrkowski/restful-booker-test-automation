package org.example.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import org.example.config.AppConfiguration;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RestClient {

  public RequestSpecification basicRequest() {
    return RestAssured.given()
        .contentType(ContentType.JSON)
        .baseUri(AppConfiguration.CONFIG.baseUrl())
        .filter(new AllureRestAssured())
        .filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
  }

}
