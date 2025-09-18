package org.example.client;

import io.restassured.response.Response;
import org.example.config.AppConfig;
import org.example.model.request.UserRequest;
import org.springframework.stereotype.Component;

@Component
public class BookerClient extends RestClient {

  public BookerClient(AppConfig appConfig) {
    super(appConfig);
  }

  public Response healthCheck() {
    return basicRequest()
        .get("/ping");
  }

  public Response createToken(UserRequest userRequest) {
    return basicRequest()
        .body(userRequest)
        .post("/auth");
  }

}
