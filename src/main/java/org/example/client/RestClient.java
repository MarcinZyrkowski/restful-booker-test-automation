package org.example.client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestClient {

  private static final String BASE_URL = "https://restful-booker.herokuapp.com";

  public RequestSpecification basicRequest() {
    return RestAssured.given()
        .contentType(ContentType.JSON)
        .baseUri(BASE_URL);
  }

}
