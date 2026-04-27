package org.example.merger.booking;

import org.example.model.dto.common.Booking;
import org.example.model.dto.common.Booking.BookingDates;
import org.example.utils.BookerRandomUtils;
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
    if (partialUpdate == null) {
      return original;
    }

    return Booking.builder()
        .firstName(
            BookerRandomUtils.nonNullValueOrDefault(
                partialUpdate.firstName(), original.firstName()))
        .lastName(
            BookerRandomUtils.nonNullValueOrDefault(partialUpdate.lastName(), original.lastName()))
        .totalPrice(
            BookerRandomUtils.nonNullValueOrDefault(
                partialUpdate.totalPrice(), original.totalPrice()))
        .depositPaid(
            BookerRandomUtils.nonNullValueOrDefault(
                partialUpdate.depositPaid(), original.depositPaid()))
        .bookingDates(
            mergeNonNullableBookingDates(original.bookingDates(), partialUpdate.bookingDates()))
        .additionalNeeds(
            BookerRandomUtils.nonNullValueOrDefault(
                partialUpdate.additionalNeeds(), original.additionalNeeds()))
        .build();
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
    if (partialUpdate == null) {
      return original;
    }

    return BookingDates.builder()
        .checkIn(
            BookerRandomUtils.nonNullValueOrDefault(partialUpdate.checkIn(), original.checkIn()))
        .checkOut(
            BookerRandomUtils.nonNullValueOrDefault(partialUpdate.checkOut(), original.checkOut()))
        .build();
  }
}
