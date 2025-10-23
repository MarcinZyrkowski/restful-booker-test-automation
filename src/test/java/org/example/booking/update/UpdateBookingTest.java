package org.example.booking.update;

import io.restassured.response.Response;
import org.example.SpringTestContext;
import org.example.factory.booking.BookingFactory;
import org.example.model.dto.common.Booking;
import org.example.model.dto.response.booking.BookingDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Update Booking")
class UpdateBookingTest extends SpringTestContext {

  @Test
  @DisplayName("Update booking with all valid fields - basic auth")
  void updateBookingUsingBasicAuthTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrGet();
    int bookingId = bookingDetails.bookingId();

    Booking bookingUpdate = BookingFactory.getWithAllValidFields();

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

    Booking bookingUpdate = BookingFactory.getWithAllValidFields();

    String token = bookerClientSteps.createToken(adminUser).token();

    Response response = bookerClient.updateBooking(bookingId, bookingUpdate, token);
    bookingAssertion.assertThat(response).statusIsOk().body().isEqualTo(bookingUpdate);

    Response fetchResponse = bookerClient.getBookingById(bookingId);
    bookingAssertion.assertThat(fetchResponse).statusIsOk().body().isEqualTo(bookingUpdate);

    bookingDetailsPool.push(
        BookingDetails.builder().bookingId(bookingId).booking(bookingUpdate).build());
  }
}
