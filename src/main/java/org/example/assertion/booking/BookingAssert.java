package org.example.assertion.booking;

import io.qameta.allure.Step;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.example.helper.booking.BookingHelper;
import org.example.model.service.dto.common.Booking;

public class BookingAssert extends AbstractAssert<BookingAssert, Booking> {

  protected BookingAssert(Booking actual) {
    super(actual, BookingAssert.class);
  }

  public static BookingAssert assertThat(Booking actual) {
    return new BookingAssert(actual);
  }

  @Step("Verify booking is equal to expected booking")
  public BookingAssert isEqualToBooking(Booking expected) {
    isNotNull();
    Assertions.assertThat(actual)
        .withFailMessage("Expected booking %s to be equal to %s", actual, expected)
        .isEqualTo(expected);
    return this;
  }

  @Step("Verify booking is partially updated")
  public BookingAssert isPartiallyUpdated(Booking original, Booking partialUpdate) {
    isNotNull();
    Booking expected = BookingHelper.mergeNonNullableBooking(original, partialUpdate);
    Assertions.assertThat(actual)
        .withFailMessage("Expected partially updated booking to be %s but was %s", expected, actual)
        .isEqualTo(expected);
    return this;
  }
}
