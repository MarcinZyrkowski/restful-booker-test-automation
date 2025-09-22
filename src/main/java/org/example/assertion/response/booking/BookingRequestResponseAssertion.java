package org.example.assertion.response.booking;

import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.example.assertion.HttpAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.request.booking.BookingRequestResponse;

public class BookingRequestResponseAssertion extends
    HttpAssertion<BookingRequestResponseAssertion> {

  private BookingRequestResponse bookingRequestResponse;

  private BookingRequestResponseAssertion(Response response) {
    super(response);
  }

  public static BookingRequestResponseAssertion assertThat(Response response) {
    return new BookingRequestResponseAssertion(response);
  }

  public BookingRequestResponseAssertion body() {
    bookingRequestResponse = ResponseMapper.map(response).toBookingRequestResponse();
    return this;
  }

  public BookingRequestResponseAssertion isEqualTo(BookingRequestResponse expected) {
    Assertions.assertThat(bookingRequestResponse)
        .isEqualTo(expected);
    return this;
  }
}
