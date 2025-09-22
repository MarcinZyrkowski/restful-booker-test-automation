package org.example.booking.fetch;

import io.restassured.response.Response;
import org.example.assertion.response.booking.BookingRequestResponseAssertion;
import org.example.assertion.response.booking.CreateBookingResponseAssertion;
import org.example.context.SpringTestContext;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.request.booking.BookingRequestResponse;
import org.junit.jupiter.api.Test;

class FetchBookingTest extends SpringTestContext {

  @Test
  void fetchBookingTest() {
    BookingRequestResponse createRequest = createBookingRequestFactory.getWithAllValidFields();

    Response createResponse = bookerClient.createBooking(createRequest);

    CreateBookingResponseAssertion.assertThat(createResponse)
        .statusIsOk();

    int bookingId = ResponseMapper.map(createResponse)
        .toCreateBookingResponse()
        .bookingId();

    Response fetchResponse = bookerClient.getBookingById(bookingId);

    BookingRequestResponseAssertion.assertThat(fetchResponse)
        .statusIsOk()
        .body()
        .isEqualTo(createRequest);
  }

}
