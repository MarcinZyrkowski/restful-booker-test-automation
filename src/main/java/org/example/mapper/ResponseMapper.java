package org.example.mapper;

import io.restassured.response.Response;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.model.response.TokenResponse;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseMapper {

  private final Response response;

  public static ResponseMapper map(Response response) {
    return new ResponseMapper(response);
  }

  private  <T> T to(Class<T> clazz) {
    return response.getBody().as(clazz);
  }

  public String toStringResponse() {
    return response.getBody().asString();
  }

  public TokenResponse toTokenResponse() {
    return to(TokenResponse.class);
  }

}
