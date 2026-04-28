package org.example.booking.update;

import io.restassured.response.Response;
import org.example.SpringTestContext;
import org.example.model.dto.common.Booking;
import org.example.model.dto.response.booking.BookingDetails;
import org.example.utils.BookerRandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Partial Update Booking")
class PartialUpdateBookingTest extends SpringTestContext {

  @Test
  @DisplayName("Partial update booking with all valid fields - basic auth")
  void partialUpdateBookingUsingBasicAuthTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    Booking partialBookingUpdate = bookingFactory.getWithValidOrNullFields();

    Response partialUpdateResponse =
        bookerClient.partialUpdateBooking(bookingId, partialBookingUpdate);
    bookingAssertion.assertBookingIsPartiallyUpdated(
        partialUpdateResponse, bookingDetails.booking(), partialBookingUpdate);

    Response fetchResponse = bookerClient.getBookingById(String.valueOf(bookingId));
    bookingAssertion.assertBookingIsPartiallyUpdated(
        fetchResponse, bookingDetails.booking(), partialBookingUpdate);
  }

  @Test
  @DisplayName("Partial update booking with all valid fields - token auth")
  void partialUpdateBookingUsingTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    Booking partialBookingUpdate = bookingFactory.getWithValidOrNullFields();
    String token = bookerClientSteps.createToken(adminUser).token();

    Response partialUpdateResponse =
        bookerClient.partialUpdateBooking(bookingId, partialBookingUpdate, token);
    bookingAssertion.assertBookingIsPartiallyUpdated(
        partialUpdateResponse, bookingDetails.booking(), partialBookingUpdate);

    Response fetchResponse = bookerClient.getBookingById(String.valueOf(bookingId));
    bookingAssertion.assertBookingIsPartiallyUpdated(
        fetchResponse, bookingDetails.booking(), partialBookingUpdate);
  }

  @Test
  @DisplayName("Should return: method not allowed when booking ID does not exist")
  void shouldNotPartialUpdateBookingWhenBookingIdDoesNotExistTest() {
    long nonExistentBookingId = BookerRandomUtils.randomNumber(100_000, 200_000);

    Booking partialBookingUpdate = bookingFactory.getWithValidOrNullFields();

    Response response =
        bookerClient.partialUpdateBooking((int) nonExistentBookingId, partialBookingUpdate);

    stringResponseAssertion.assertResponseIsMethodNotAllowed(response);
  }

  @Test
  @DisplayName("Should return: forbidden when updating booking with invalid token")
  void shouldNotPartialUpdateBookingWithInvalidTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    Booking partialBookingUpdate = bookingFactory.getWithValidOrNullFields();
    String invalidToken = "invalid_token";

    Response response =
        bookerClient.partialUpdateBooking(bookingId, partialBookingUpdate, invalidToken);

    stringResponseAssertion.assertResponseIsForbidden(response);

    bookingDetailsPool.push(bookingDetails);
  }
}
