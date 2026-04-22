package org.example.booking.delete;

import io.restassured.response.Response;
import org.example.SpringTestContext;
import org.example.model.dto.response.booking.BookingDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Delete Booking")
class DeleteBookingTest extends SpringTestContext {

  @Test
  @DisplayName("Delete booking using basic auth")
  void deleteBookingUsingBasicAuthTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    Response deleteResponse = bookerClient.deleteBooking(bookingId);
    stringResponseAssertion.assertResponseIsCreated(deleteResponse);

    bookerClientSteps.fetchBookingAssertNotFound(bookingId);
  }

  @Test
  @DisplayName("Delete booking using token")
  void deleteBookingUsingTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    String token = bookerClientSteps.createToken(adminUser).token();

    Response deleteResponse = bookerClient.deleteBooking(bookingId, token);
    stringResponseAssertion.assertResponseIsCreated(deleteResponse);

    bookerClientSteps.fetchBookingAssertNotFound(bookingId);
  }

  @Test
  @DisplayName("Delete booking using invalid token")
  void deleteBookingUsingInvalidTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    String invalidToken = "invalid-token";
    Response deleteResponse = bookerClient.deleteBooking(bookingId, invalidToken);

    stringResponseAssertion.assertResponseIsForbidden(deleteResponse);
  }
}
