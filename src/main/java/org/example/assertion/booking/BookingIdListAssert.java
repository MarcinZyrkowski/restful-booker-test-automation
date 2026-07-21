package org.example.assertion.booking;

import java.util.List;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.example.model.service.dto.response.booking.BookingId;

public class BookingIdListAssert extends AbstractAssert<BookingIdListAssert, List<BookingId>> {

  protected BookingIdListAssert(List<BookingId> actual) {
    super(actual, BookingIdListAssert.class);
  }

  public static BookingIdListAssert assertThat(List<BookingId> actual) {
    return new BookingIdListAssert(actual);
  }

  public BookingIdListAssert isNotEmpty() {
    isNotNull();
    Assertions.assertThat(actual).isNotEmpty();
    return this;
  }

  public BookingIdListAssert isEmpty() {
    isNotNull();
    Assertions.assertThat(actual).isEmpty();
    return this;
  }

  public BookingIdListAssert containsBookingId(int bookingId) {
    isNotNull();
    Assertions.assertThat(actual).extracting(BookingId::bookingId).contains(bookingId);
    return this;
  }
}
