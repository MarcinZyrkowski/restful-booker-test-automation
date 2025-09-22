package org.example.booking.delete;

import io.restassured.response.Response;
import org.example.assertion.response.StringResponseAssertion;
import org.example.assertion.response.booking.CreateBookingResponseAssertion;
import org.example.context.SpringTestContext;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.request.booking.BookingRequestResponse;
import org.example.model.enums.service.StringResponseBody;
import org.junit.jupiter.api.Test;

class DeleteBookingTest extends SpringTestContext {

  @Test
  void deleteBookingWithBasicAuthTest() {
    BookingRequestResponse createRequest = createBookingRequestFactory.getWithAllValidFields();

    Response createResponse = bookerClient.createBooking(createRequest);

    CreateBookingResponseAssertion.assertThat(createResponse)
        .statusIsOk();

    int bookingId = ResponseMapper.map(createResponse).toCreateBookingResponse().bookingId();

    Response deleteResponse = bookerClient.deleteBooking(bookingId);

    StringResponseAssertion.assertThat(deleteResponse)
        .statusIsCreated()
        .body()
        .isEqualTo(StringResponseBody.CREATED.getBody());

    Response getResponse = bookerClient.getBookingById(bookingId);

    StringResponseAssertion.assertThat(getResponse)
        .statusIsNotFound()
        .body()
        .isEqualTo(StringResponseBody.NOT_FOUND.getBody());
  }

  // TODO add test case with token auth

}
