package org.example.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.example.config.SpringConfig;
import org.example.model.service.dto.common.Booking;
import org.example.model.service.dto.request.auth.User;
import org.example.utils.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
public class BookerApi extends RestApi {

  private static final String HEALTH_CHECK_ENDPOINT = "/ping";
  private static final String AUTH_ENDPOINT = "/auth";
  private static final String BOOKING_ENDPOINT = "/booking";
  private static final String BOOKING_ID_ENDPOINT = "/booking/{id}";

  private final AuthRequestDecorator authRequestDecorator;

  public BookerApi(SpringConfig springConfig, AuthRequestDecorator authRequestDecorator) {
    super(springConfig);
    this.authRequestDecorator = authRequestDecorator;
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
    return authRequestDecorator
        .withBasicAuth(basicRequest())
        .body(booking)
        .put(BOOKING_ID_ENDPOINT, bookingId);
  }

  @Step("Update booking with id: {bookingId} using token")
  public Response updateBooking(int bookingId, Booking booking, String token) {
    return authRequestDecorator
        .withTokenAuth(basicRequest(), token)
        .body(booking)
        .put(BOOKING_ID_ENDPOINT, bookingId);
  }

  @Step("Partial update booking with id: {bookingId}")
  public Response partialUpdateBooking(int bookingId, Booking booking) {
    return authRequestDecorator
        .withBasicAuth(basicRequest())
        .body(booking)
        .patch(BOOKING_ID_ENDPOINT, bookingId);
  }

  @Step("Partial update booking with id: {bookingId} using token")
  public Response partialUpdateBooking(int bookingId, Booking booking, String token) {
    return authRequestDecorator
        .withTokenAuth(basicRequest(), token)
        .body(booking)
        .patch(BOOKING_ID_ENDPOINT, bookingId);
  }

  @Step("Get booking by id: {bookingId}")
  public Response getBookingById(String bookingId) {
    return basicRequest().get(BOOKING_ID_ENDPOINT, bookingId);
  }

  @Step("Delete booking with id: {bookingId} using basic auth")
  public Response deleteBooking(int bookingId) {
    return authRequestDecorator
        .withBasicAuth(basicRequest())
        .delete(BOOKING_ID_ENDPOINT, bookingId);
  }

  @Step("Delete booking with id: {bookingId} using token")
  public Response deleteBooking(int bookingId, String token) {
    return authRequestDecorator
        .withTokenAuth(basicRequest(), token)
        .delete(BOOKING_ID_ENDPOINT, bookingId);
  }
}
