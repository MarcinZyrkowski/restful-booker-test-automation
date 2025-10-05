package org.example.booking.delete;

import io.restassured.response.Response;
import org.example.assertion.response.StringResponseAssertion;
import org.example.context.SpringTestContext;
import org.example.factory.auth.UserRequestFactory;
import org.example.factory.booking.CreateBookingRequestFactory;
import org.example.model.dto.common.Booking;
import org.example.model.dto.request.auth.User;
import org.example.model.enums.service.StringResponseBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Delete Booking")
class DeleteBookingTest extends SpringTestContext {

  @Test
  @DisplayName("Delete booking with basic auth")
  void deleteBookingWithBasicAuthTest() {
    Booking createRequest = CreateBookingRequestFactory.getWithAllValidFields();
    int bookingId = bookerClientSteps.createBooking(createRequest)
        .bookingId();

    Response deleteResponse = bookerClient.deleteBooking(bookingId);
    StringResponseAssertion.assertThat(deleteResponse)
        .statusIsCreated()
        .body()
        .isEqualTo(StringResponseBody.CREATED.getBody());

    bookerClientSteps.fetchBookingAssertNotFound(bookingId);
  }

  @Test
  @DisplayName("Delete booking with token")
  void deleteBookingWithTokenTest() {
    Booking createRequest = CreateBookingRequestFactory.getWithAllValidFields();
    int bookingId = bookerClientSteps.createBooking(createRequest)
        .bookingId();

    User user = UserRequestFactory.defaultUser();
    String token = bookerClientSteps.createToken(user)
        .token();

    Response deleteResponse = bookerClient.deleteBooking(bookingId, token);
    StringResponseAssertion.assertThat(deleteResponse)
        .statusIsCreated()
        .body()
        .isEqualTo(StringResponseBody.CREATED.getBody());

    bookerClientSteps.fetchBookingAssertNotFound(bookingId);
  }

}
