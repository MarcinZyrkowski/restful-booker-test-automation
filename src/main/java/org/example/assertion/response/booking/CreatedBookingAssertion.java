package org.example.assertion.response.booking;

import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.example.assertion.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.common.Booking;
import org.example.model.dto.response.booking.CreatedBooking;

public class CreatedBookingAssertion extends
    ResponseAssertion<CreatedBookingAssertion> {

  private CreatedBooking createdBooking;

  private CreatedBookingAssertion(Response response) {
    super(response);
  }

  public static CreatedBookingAssertion assertThat(Response response) {
    return new CreatedBookingAssertion(response);
  }

  public CreatedBookingAssertion body() {
    createdBooking = ResponseMapper.map(response).toCreateBookingResponse();
    return this;
  }

  public CreatedBookingAssertion isCreatedFrom(
      Booking booking) {
    Assertions.assertThat(createdBooking).isNotNull();
    Assertions.assertThat(createdBooking.booking()).isNotNull();
    Assertions.assertThat(createdBooking.bookingId()).isNotNull();

    Assertions.assertThat(createdBooking.booking())
        .isEqualTo(booking);
    return this;
  }
}
