package org.example.dataprovider;

import lombok.RequiredArgsConstructor;
import org.example.factory.booking.BookingFactory;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class BookingDataProvider {

  private final BookingFactory bookingFactory;

  public Stream<Arguments> missingFieldBookings() {
    return Stream.of(
        Arguments.of(bookingFactory.getWithMissingFirstName(), "missing: first name"),
        Arguments.of(bookingFactory.getWithMissingLastName(), "missing: last name"),
        Arguments.of(bookingFactory.getWithMissingTotalPrice(), "missing: total price"),
        Arguments.of(bookingFactory.getWithMissingDepositPaid(), "missing: deposit paid"),
        Arguments.of(bookingFactory.getWithMissingBookingDates(), "missing: booking dates"),
        Arguments.of(bookingFactory.getWithMissingCheckin(), "missing: checkin"),
        Arguments.of(bookingFactory.getWithMissingCheckout(), "missing: checkout"));
  }
}
