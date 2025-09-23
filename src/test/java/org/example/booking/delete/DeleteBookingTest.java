package org.example.booking.delete;

import io.restassured.response.Response;
import org.example.assertion.response.StringResponseAssertion;
import org.example.assertion.response.booking.CreateBookingResponseAssertion;
import org.example.context.SpringTestContext;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.common.BookingRequestResponse;
import org.example.model.dto.request.auth.UserRequest;
import org.example.model.enums.service.StringResponseBody;
import org.example.steps.BookerClientSteps;
import org.junit.jupiter.api.Test;

class DeleteBookingTest extends SpringTestContext {

  @Test
  void deleteBookingWithBasicAuthTest() {
    BookingRequestResponse createRequest = createBookingRequestFactory.getWithAllValidFields();
    int bookingId = bookerClientSteps.createBooking(createRequest).bookingId();

    Response deleteResponse = bookerClient.deleteBooking(bookingId);
    StringResponseAssertion.assertThat(deleteResponse)
        .statusIsCreated()
        .body()
        .isEqualTo(StringResponseBody.CREATED.getBody());

    bookerClientSteps.verifyBookingNotFound(bookingId);
  }

  @Test
  void deleteBookingWithTokenTest() {
    BookingRequestResponse createRequest = createBookingRequestFactory.getWithAllValidFields();
    int bookingId = bookerClientSteps.createBooking(createRequest).bookingId();

    UserRequest userRequest = userRequestFactory.defaultUser();
    String token = bookerClientSteps.createToken(userRequest).token();

    Response deleteResponse = bookerClient.deleteBooking(bookingId, token);
    StringResponseAssertion.assertThat(deleteResponse)
        .statusIsCreated()
        .body()
        .isEqualTo(StringResponseBody.CREATED.getBody());

    bookerClientSteps.verifyBookingNotFound(bookingId);
  }

}
