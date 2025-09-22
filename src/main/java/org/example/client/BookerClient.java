package org.example.client;

import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.example.config.AppConfig;
import org.example.model.dto.request.auth.UserRequest;
import org.example.model.dto.request.booking.BookingRequestResponse;
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
      String firstName,
      String lastName,
      String checkIn,
      String checkOut
  ) {
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("firstname", firstName);
    queryParams.put("lastname", lastName);
    queryParams.put("checkin", checkIn);
    queryParams.put("checkout", checkOut);

    Map<String, Object> filtered = CollectionUtils.filterNonNullValues(queryParams);

    return basicRequest()
        .queryParams(filtered)
        .get("/booking");
  }

  public Response createBooking(BookingRequestResponse bookingRequestResponse) {
    return basicRequest()
        .body(bookingRequestResponse)
        .post("/booking");
  }

  public Response getBookingById(int bookingId) {
    return basicRequest()
        .get("/booking/" + bookingId);
  }
}


