package org.example.dataprovider;

import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.example.factory.booking.BookingFactory;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingDataProvider {

  private final BookingFactory bookingFactory;

  public Stream<Arguments> missingFieldBookings() {
    return Stream.of(
        Arguments.of(bookingFactory.getWithMissingFirstName(), "missing: first name"),
        Arguments.of(bookingFactory.getWithMissingLastName(), "missing: last name"));
  }
}
