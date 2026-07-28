package org.example.assertion.booking;

import io.qameta.allure.Step;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.example.model.service.dto.common.Booking;
import org.example.model.service.dto.response.booking.BookingDetails;

public class BookingDetailsAssert extends AbstractAssert<BookingDetailsAssert, BookingDetails> {

  protected BookingDetailsAssert(BookingDetails actual) {
    super(actual, BookingDetailsAssert.class);
  }

  public static BookingDetailsAssert assertThat(BookingDetails actual) {
    return new BookingDetailsAssert(actual);
  }

  @Step("Verify booking details are created from expected booking")
  public BookingDetailsAssert isCreatedFrom(Booking expectedBooking) {
    isNotNull();

    Assertions.assertThat(actual.bookingId())
        .withFailMessage("Expected booking ID to be not null")
        .isNotNull();

    Assertions.assertThat(actual.booking())
        .withFailMessage("Expected booking to match the expected booking request")
        .isEqualTo(expectedBooking);

    return this;
  }
}
