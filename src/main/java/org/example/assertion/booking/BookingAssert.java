package org.example.assertion.booking;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.example.helper.booking.BookingHelper;
import org.example.model.service.dto.common.Booking;

public class BookingAssert extends AbstractAssert<BookingAssert, Booking> {

  private final BookingHelper bookingHelper = new BookingHelper();

  protected BookingAssert(Booking actual) {
    super(actual, BookingAssert.class);
  }

  public static BookingAssert assertThat(Booking actual) {
    return new BookingAssert(actual);
  }

  public BookingAssert isEqualToBooking(Booking expected) {
    isNotNull();
    Assertions.assertThat(actual).isEqualTo(expected);
    return this;
  }

  public BookingAssert isPartiallyUpdated(Booking original, Booking partialUpdate) {
    isNotNull();
    Booking expected = bookingHelper.mergeNonNullableBooking(original, partialUpdate);
    Assertions.assertThat(actual).isEqualTo(expected);
    return this;
  }
}
