package org.example.booking.delete;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.example.SpringTestContext;
import org.example.assertion.response.StringResponseAssertion;
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

    Allure.step(
        "Verify booking is deleted successfully",
        () -> {
          StringResponseAssertion.assertThat(deleteResponse)
              .statusIsCreated()
              .body()
              .isEqualTo(StringResponseBody.CREATED.getBody());
        });

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

    Allure.step(
        "Verify booking is deleted successfully",
        () -> {
          StringResponseAssertion.assertThat(deleteResponse)
              .statusIsCreated()
              .body()
              .isEqualTo(StringResponseBody.CREATED.getBody());
        });

    bookerClientSteps.fetchBookingAssertNotFound(bookingId);
  }
}
