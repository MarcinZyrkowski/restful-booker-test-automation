package org.example.helper.merger;

import java.util.Optional;
import org.example.model.dto.common.Booking;
import org.example.model.dto.common.Booking.BookingBuilder;
import org.example.model.dto.common.Booking.BookingDates;
import org.example.model.dto.common.Booking.BookingDates.BookingDatesBuilder;
import org.springframework.stereotype.Component;

@Component
public class BookingMerger {

  /**
   * Merges a partial Booking update into the original Booking. Non-null values from the partial
   * update override values from the original.
   *
   * @param original the original booking
   * @param partialUpdate the partial booking with updates
   * @return a merged booking with non-null values from partialUpdate
   */
  public Booking mergeNonNullableBooking(Booking original, Booking partialUpdate) {
    if (partialUpdate == null) return original;
    if (original == null) return partialUpdate;

    BookingBuilder builder = original.toBuilder();
    Optional.ofNullable(partialUpdate.firstName()).ifPresent(builder::firstName);
    Optional.ofNullable(partialUpdate.lastName()).ifPresent(builder::lastName);
    Optional.ofNullable(partialUpdate.totalPrice()).ifPresent(builder::totalPrice);
    Optional.ofNullable(partialUpdate.depositPaid()).ifPresent(builder::depositPaid);
    Optional.ofNullable(partialUpdate.additionalNeeds()).ifPresent(builder::additionalNeeds);

    if (partialUpdate.bookingDates() != null) {
      builder.bookingDates(
          mergeNonNullableBookingDates(original.bookingDates(), partialUpdate.bookingDates()));
    }

    return builder.build();
  }

  /**
   * Merges a partial BookingDates update into the original BookingDates. Non-null values from the
   * partial update override values from the original.
   *
   * @param original the original booking dates
   * @param partialUpdate the partial booking dates with updates
   * @return a merged booking dates with non-null values from partialUpdate
   */
  public BookingDates mergeNonNullableBookingDates(
      BookingDates original, BookingDates partialUpdate) {
    if (partialUpdate == null) return original;
    if (original == null) return partialUpdate;

    BookingDatesBuilder builder = original.toBuilder();
    Optional.ofNullable(partialUpdate.checkIn()).ifPresent(builder::checkIn);
    Optional.ofNullable(partialUpdate.checkOut()).ifPresent(builder::checkOut);

    return builder.build();
  }
}
