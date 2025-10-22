package org.example.client;

import io.qameta.allure.Step;
import io.restassured.http.Header;
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

  private Header tokenCookieHeader(String token) {
    return new Header("Cookie", "token=" + token);
  }

  @Step("Health check")
  public Response healthCheck() {
    return basicRequest().get(HEALTH_CHECK_ENDPOINT);
  }

  @Step("Create auth token")
  public Response createToken(User user) {
    return basicRequest().body(user).post(AUTH_ENDPOINT);
  }

  @Step("Get booking IDs with filters")
  public Response getBookingIds(
      String firstName, String lastName, String checkIn, String checkOut) {
    Map<String, Object> queryParams = new HashMap<>();
    queryParams.put("firstname", firstName);
    queryParams.put("lastname", lastName);
    queryParams.put("checkin", checkIn);
    queryParams.put("checkout", checkOut);

    Map<String, Object> filtered = CollectionUtils.filterNonNullValues(queryParams);

    return basicRequest().queryParams(filtered).get(BOOKING_ENDPOINT);
  }

  @Step("Create booking")
  public Response createBooking(Booking booking) {
    return basicRequest().body(booking).post(BOOKING_ENDPOINT);
  }

  @Step("Update booking with id: {bookingId}")
  public Response updateBooking(int bookingId, Booking booking) {
    return basicRequest()
        .auth()
        .preemptive()
        .basic(AppConfiguration.CONFIG.username(), AppConfiguration.CONFIG.password())
        .body(booking)
        .put(BOOKING_ID_ENDPOINT, bookingId);
  }

  @Step("Update booking with id: {bookingId} using token")
  public Response updateBooking(int bookingId, Booking booking, String token) {
    return basicRequest()
        .header(tokenCookieHeader(token))
        .body(booking)
        .put(BOOKING_ID_ENDPOINT, bookingId);
  }

  @Step("Partial update booking with id: {bookingId}")
  public Response partialUpdateBooking(int bookingId, Booking booking) {
    return basicRequest()
        .auth()
        .preemptive()
        .basic(AppConfiguration.CONFIG.username(), AppConfiguration.CONFIG.password())
        .body(booking)
        .patch(BOOKING_ID_ENDPOINT, bookingId);
  }

  @Step("Partial update booking with id: {bookingId} using token")
  public Response partialUpdateBooking(int bookingId, Booking booking, String token) {
    return basicRequest()
        .header(tokenCookieHeader(token))
        .body(booking)
        .patch(BOOKING_ID_ENDPOINT, bookingId);
  }

  @Step("Get booking by id: {bookingId}")
  public Response getBookingById(int bookingId) {
    return basicRequest().get(BOOKING_ID_ENDPOINT, bookingId);
  }

  @Step("Delete booking with id: {bookingId} using basic auth")
  public Response deleteBooking(int bookingId) {
    return basicRequest()
        .auth()
        .preemptive()
        .basic(AppConfiguration.CONFIG.username(), AppConfiguration.CONFIG.password())
        .delete(BOOKING_ID_ENDPOINT, bookingId);
  }

  @Step("Delete booking with id: {bookingId} using token")
  public Response deleteBooking(int bookingId, String token) {
    return basicRequest().header(tokenCookieHeader(token)).delete(BOOKING_ID_ENDPOINT, bookingId);
  }
}
