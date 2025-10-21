package org.example.assertion.response.booking;

import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.example.assertion.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.common.Booking;

public class BookingAssertion extends ResponseAssertion<BookingAssertion> {

  private Booking booking;

  private BookingAssertion(Response response) {
    super(response);
  }

  public static BookingAssertion assertThat(Response response) {
    return new BookingAssertion(response);
  }

  public BookingAssertion body() {
    booking = ResponseMapper.map(response).toBookingRequestResponse();
    return this;
  }

  public BookingAssertion isEqualTo(Booking expected) {
    Assertions.assertThat(booking).isEqualTo(expected);
    return this;
  }
}
