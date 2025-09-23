package org.example.booking.fetch;

import io.restassured.response.Response;
import org.example.assertion.response.booking.BookingRequestResponseAssertion;
import org.example.context.SpringTestContext;
import org.example.model.dto.common.BookingRequestResponse;
import org.junit.jupiter.api.Test;

class FetchBookingTest extends SpringTestContext {

  @Test
  void fetchBookingTest() {
    BookingRequestResponse createRequest = createBookingRequestFactory.getWithAllValidFields();
    int bookingId = bookerClientSteps.createBooking(createRequest).bookingId();

    Response fetchResponse = bookerClient.getBookingById(bookingId);

    BookingRequestResponseAssertion.assertThat(fetchResponse)
        .statusIsOk()
        .body()
        .isEqualTo(createRequest);
  }

}
