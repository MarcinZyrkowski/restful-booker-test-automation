package org.example.assertion.response.booking;

import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.example.assertion.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.common.Booking;
import org.example.model.dto.response.booking.BookingDetails;

public class BookingDetailsAssertion extends ResponseAssertion<BookingDetailsAssertion> {

  private BookingDetails bookingDetails;

  private BookingDetailsAssertion(Response response) {
    super(response);
  }

  public static BookingDetailsAssertion assertThat(Response response) {
    return new BookingDetailsAssertion(response);
  }

  public BookingDetailsAssertion body() {
    bookingDetails = ResponseMapper.map(response).toCreateBookingResponse();
    return this;
  }

  public BookingDetailsAssertion isCreatedFrom(Booking booking) {
    Assertions.assertThat(bookingDetails).isNotNull();
    Assertions.assertThat(bookingDetails.booking()).isNotNull();
    Assertions.assertThat(bookingDetails.bookingId()).isNotNull();

    Assertions.assertThat(bookingDetails.booking()).isEqualTo(booking);
    return this;
  }
}
