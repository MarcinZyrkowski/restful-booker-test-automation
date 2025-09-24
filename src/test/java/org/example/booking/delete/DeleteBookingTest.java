package org.example.booking.delete;

import io.restassured.response.Response;
import org.example.assertion.response.StringResponseAssertion;
import org.example.context.SpringTestContext;
import org.example.factory.auth.UserRequestFactory;
import org.example.factory.booking.CreateBookingRequestFactory;
import org.example.model.dto.common.BookingRequestResponse;
import org.example.model.dto.request.auth.UserRequest;
import org.example.model.enums.service.StringResponseBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Delete Booking")
class DeleteBookingTest extends SpringTestContext {

  @Test
  @DisplayName("Delete booking with basic auth")
  void deleteBookingWithBasicAuthTest() {
    BookingRequestResponse createRequest = CreateBookingRequestFactory.getWithAllValidFields();
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
    BookingRequestResponse createRequest = CreateBookingRequestFactory.getWithAllValidFields();
    int bookingId = bookerClientSteps.createBooking(createRequest)
        .bookingId();

    UserRequest userRequest = UserRequestFactory.defaultUser();
    String token = bookerClientSteps.createToken(userRequest)
        .token();

    Response deleteResponse = bookerClient.deleteBooking(bookingId, token);
    StringResponseAssertion.assertThat(deleteResponse)
        .statusIsCreated()
        .body()
        .isEqualTo(StringResponseBody.CREATED.getBody());

    bookerClientSteps.fetchBookingAssertNotFound(bookingId);
  }

}
