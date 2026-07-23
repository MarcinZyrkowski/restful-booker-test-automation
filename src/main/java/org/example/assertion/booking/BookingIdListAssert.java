package org.example.assertion.booking;

import io.qameta.allure.Step;
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

  @Step("Verify booking ID list is not empty")
  public BookingIdListAssert isNotEmpty() {
    isNotNull();
    Assertions.assertThat(actual)
        .withFailMessage("Expected booking ID list to not be empty, but it was")
        .isNotEmpty();
    return this;
  }

  @Step("Verify booking ID list is empty")
  public BookingIdListAssert isEmpty() {
    isNotNull();
    Assertions.assertThat(actual)
        .withFailMessage("Expected booking ID list to be empty, but it contained elements")
        .isEmpty();
    return this;
  }

  @Step("Verify booking ID list contains {bookingId}")
  public BookingIdListAssert containsBookingId(int bookingId) {
    isNotNull();
    Assertions.assertThat(actual)
        .extracting(BookingId::bookingId)
        .withFailMessage("Expected booking ID list to contain %d", bookingId)
        .contains(bookingId);
    return this;
  }
}
