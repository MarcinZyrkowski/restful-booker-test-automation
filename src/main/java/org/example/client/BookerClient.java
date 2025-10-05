package org.example.client;

import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.example.config.AppConfiguration;
import org.example.model.dto.common.Booking;
import org.example.model.dto.request.auth.User;
import org.example.utils.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
public class BookerClient extends RestClient {

  private static final String HEALTH_CHECK_ENDPOINT = "/ping";
  private static final String AUTH_ENDPOINT = "/auth";
  private static final String BOOKING_ENDPOINT = "/booking";
  private static final String BOOKING_ID_ENDPOINT = "/booking/{id}";

  public Response healthCheck() {
    return basicRequest()
        .get(HEALTH_CHECK_ENDPOINT);
  }

  public Response createToken(User user) {
    return basicRequest()
        .body(user)
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

  public Response createBooking(Booking booking) {
    return basicRequest()
        .body(booking)
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
        .basic(AppConfiguration.CONFIG.username(), AppConfiguration.CONFIG.password())
        .delete(BOOKING_ID_ENDPOINT, bookingId);
  }

  public Response deleteBooking(int bookingId, String token) {
    return basicRequest()
        .header("Cookie", "token=" + token)
        .delete(BOOKING_ID_ENDPOINT, bookingId);
  }
}


