package org.example.client;

import io.restassured.response.Response;
import org.example.config.Config;
import org.springframework.stereotype.Component;

@Component
public class BookerClient extends RestClient {

  public BookerClient(Config config) {
    super(config);
  }

  public Response healthCheck() {
    return basicRequest()
        .get("/ping");
  }

}
