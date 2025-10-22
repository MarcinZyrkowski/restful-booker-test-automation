package org.example.booking.delete;

import io.restassured.response.Response;
import org.example.SpringTestContext;
import org.example.factory.auth.UserRequestFactory;
import org.example.model.dto.request.auth.User;
import org.example.model.dto.response.booking.BookingDetails;
import org.example.model.enums.service.StringResponseBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Delete Booking")
class DeleteBookingTest extends SpringTestContext {

  @Test
  @DisplayName("Delete booking using basic auth")
  void deleteBookingUsingBasicAuthTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrGet();
    int bookingId = bookingDetails.bookingId();

    Response deleteResponse = bookerClient.deleteBooking(bookingId);

    stringResponseAssertion
        .assertThat(deleteResponse)
        .statusIsCreated()
        .body()
        .isEqualTo(StringResponseBody.CREATED.getBody());

    bookerClientSteps.fetchBookingAssertNotFound(bookingId);
  }

  @Test
  @DisplayName("Delete booking using token")
  void deleteBookingUsingTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrGet();
    int bookingId = bookingDetails.bookingId();

    User user = UserRequestFactory.defaultUser();

    String token = bookerClientSteps.createToken(user).token();

    Response deleteResponse = bookerClient.deleteBooking(bookingId, token);

    stringResponseAssertion
        .assertThat(deleteResponse)
        .statusIsCreated()
        .body()
        .isEqualTo(StringResponseBody.CREATED.getBody());

    bookerClientSteps.fetchBookingAssertNotFound(bookingId);
  }
}
