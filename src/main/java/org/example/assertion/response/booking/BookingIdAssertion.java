package org.example.assertion.response.booking;

import io.restassured.response.Response;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.example.assertion.HttpAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.response.booking.BookingIdResponse;

public class BookingIdAssertion extends HttpAssertion<BookingIdAssertion> {

  private List<BookingIdResponse> bookingIdResponseList;

  private BookingIdAssertion(Response response) {
    super(response);
  }

  public static BookingIdAssertion assertThat(Response response) {
    return new BookingIdAssertion(response);
  }

  public BookingIdAssertion body() {
    bookingIdResponseList = ResponseMapper
        .map(response)
        .toBookingIdResponseList();
    return this;
  }

  public BookingIdAssertion hasBookingIds() {
    Assertions.assertThat(bookingIdResponseList)
        .isNotNull()
        .isNotEmpty();
    return this;
  }
}
