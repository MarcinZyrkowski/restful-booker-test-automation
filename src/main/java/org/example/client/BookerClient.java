package org.example.client;

import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.example.config.AppConfig;
import org.example.model.request.UserRequest;
import org.example.utils.CollectionUtils;
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

  public Response getBookingIds(
      String firstname,
      String lastname,
      String checkin,
      String checkout
  ) {
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("firstname", firstname);
    queryParams.put("lastname", lastname);
    queryParams.put("checkin", checkin);
    queryParams.put("checkout", checkout);

    Map<String, Object> filtered = CollectionUtils.filterNonNullValues(queryParams);

    return basicRequest()
        .queryParams(filtered)
        .get("/booking");
  }
}


