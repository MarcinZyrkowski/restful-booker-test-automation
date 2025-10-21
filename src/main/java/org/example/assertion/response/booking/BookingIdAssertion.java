package org.example.assertion.response.booking;

import io.restassured.response.Response;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.example.assertion.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.response.booking.BookingId;

public class BookingIdAssertion extends ResponseAssertion<BookingIdAssertion> {

  private List<BookingId> bookingIdList;

  private BookingIdAssertion(Response response) {
    super(response);
  }

  public static BookingIdAssertion assertThat(Response response) {
    return new BookingIdAssertion(response);
  }

  public BookingIdAssertion body() {
    bookingIdList = ResponseMapper.map(response).toBookingIdResponseList();
    return this;
  }

  public BookingIdAssertion hasBookingIds() {
    Assertions.assertThat(bookingIdList).isNotNull().isNotEmpty();
    return this;
  }
}
