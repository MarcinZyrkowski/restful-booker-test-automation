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

  private static final String HEALTH_CHECK_ENDPOINT = "/ping";
  private static final String AUTH_ENDPOINT = "/auth";
  private static final String BOOKING_ENDPOINT = "/booking";
  private static final String BOOKING_ID_ENDPOINT = "/booking/{id}";

  public BookerClient(AppConfig appConfig) {
    super(appConfig);
  }

  public Response healthCheck() {
    return basicRequest()
        .get(HEALTH_CHECK_ENDPOINT);
  }

  public Response createToken(UserRequest userRequest) {
    return basicRequest()
        .body(userRequest)
        .post(AUTH_ENDPOINT);
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
        .get(BOOKING_ENDPOINT);
  }

  public Response createBooking(BookingRequestResponse bookingRequestResponse) {
    return basicRequest()
        .body(bookingRequestResponse)
        .post(BOOKING_ENDPOINT);
  }

  public Response getBookingById(int bookingId) {
    return basicRequest()
        .get(BOOKING_ID_ENDPOINT, bookingId);
  }

  public Response deleteBooking(int bookingId) {
    return basicRequest()
        .auth()
        .preemptive()
        .basic(appConfig.getUsername(), appConfig.getPassword())
        .delete(BOOKING_ID_ENDPOINT, bookingId);
  }

  public Response deleteBooking(int bookingId, String token) {
    return basicRequest()
        .header("Cookie", "token=" + token)
        .delete(BOOKING_ID_ENDPOINT, bookingId);
  }
}


