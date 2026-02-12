package org.example.booking.update;

import io.restassured.response.Response;
import org.example.SpringTestContext;
import org.example.model.dto.common.Booking;
import org.example.model.dto.response.booking.BookingDetails;
import org.example.model.enums.service.StringResponseBody;
import org.example.utils.BookerRandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Update Booking")
class UpdateBookingTest extends SpringTestContext {

  @Test
  @DisplayName("Update booking with all valid fields - basic auth")
  void updateBookingUsingBasicAuthTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrGet();
    int bookingId = bookingDetails.bookingId();

    Booking bookingUpdate = bookingFactory.getWithAllValidFields();

    Response response = bookerClient.updateBooking(bookingId, bookingUpdate);
    bookingAssertion.assertThat(response).statusIsOk().body().isEqualTo(bookingUpdate);

    Response fetchResponse = bookerClient.getBookingById(bookingId);
    bookingAssertion.assertThat(fetchResponse).statusIsOk().body().isEqualTo(bookingUpdate);

    bookingDetailsPool.push(
        BookingDetails.builder().bookingId(bookingId).booking(bookingUpdate).build());
  }

  @Test
  @DisplayName("Update booking with all valid fields - token auth")
  void updateBookingUsingTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrGet();
    int bookingId = bookingDetails.bookingId();

    Booking bookingUpdate = bookingFactory.getWithAllValidFields();

    String token = bookerClientSteps.createToken(adminUser).token();

    Response response = bookerClient.updateBooking(bookingId, bookingUpdate, token);
    bookingAssertion.assertThat(response).statusIsOk().body().isEqualTo(bookingUpdate);

    Response fetchResponse = bookerClient.getBookingById(bookingId);
    bookingAssertion.assertThat(fetchResponse).statusIsOk().body().isEqualTo(bookingUpdate);

    bookingDetailsPool.push(
        BookingDetails.builder().bookingId(bookingId).booking(bookingUpdate).build());
  }

  @Test
  @DisplayName("Should return: forbidden when updating booking with invalid token")
  void shouldNotUpdateBookingWithInvalidTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrGet();
    int bookingId = bookingDetails.bookingId();

    Booking bookingUpdate = bookingFactory.getWithAllValidFields();

    String invalidToken = "invalid_token";

    Response response = bookerClient.updateBooking(bookingId, bookingUpdate, invalidToken);

    stringResponseAssertion
        .assertThat(response)
        .statusIsForbidden()
        .body()
        .isEqualTo(StringResponseBody.FORBIDDEN.getBody());

    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Should return: method not allowed when booking ID does not exist")
  void shouldNotUpdateBookingWhenBookingIdDoesNotExistTest() {
    int nonExistentBookingId = BookerRandomUtils.RANDOM.randomInt(100000, 200000);

    Booking bookingUpdate = bookingFactory.getWithAllValidFields();

    Response response = bookerClient.updateBooking(nonExistentBookingId, bookingUpdate);

    stringResponseAssertion
        .assertThat(response)
        .statusIsMethodNotAllowed()
        .body()
        .isEqualTo(StringResponseBody.METHOD_NOT_ALLOWED.getBody());
  }
}
