package org.example.assertion.response.booking;

import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.example.assertion.HttpAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.common.BookingRequestResponse;
import org.example.model.dto.response.booking.CreateBookingResponse;

public class CreateBookingResponseAssertion extends HttpAssertion<CreateBookingResponseAssertion> {

  private CreateBookingResponse createBookingResponse;

  private CreateBookingResponseAssertion(Response response) {
    super(response);
  }

  public static CreateBookingResponseAssertion assertThat(Response response) {
    return new CreateBookingResponseAssertion(response);
  }

  public CreateBookingResponseAssertion body() {
    createBookingResponse = ResponseMapper.map(response).toCreateBookingResponse();
    return this;
  }

  public CreateBookingResponseAssertion isCreatedFrom(
      BookingRequestResponse bookingRequestResponse) {
    Assertions.assertThat(createBookingResponse).isNotNull();
    Assertions.assertThat(createBookingResponse.booking()).isNotNull();
    Assertions.assertThat(createBookingResponse.bookingId()).isNotNull();

    Assertions.assertThat(createBookingResponse.booking())
        .isEqualTo(bookingRequestResponse);
    return this;
  }
}
