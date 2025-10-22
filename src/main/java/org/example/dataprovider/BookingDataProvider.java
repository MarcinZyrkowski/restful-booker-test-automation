package org.example.dataprovider;

import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.factory.booking.BookingFactory;
import org.junit.jupiter.params.provider.Arguments;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingDataProvider {

  public static Stream<Arguments> missingFieldBookings() {
    return Stream.of(
        Arguments.of(BookingFactory.getWithMissingFirstName(), "missing: first name"),
        Arguments.of(BookingFactory.getWithMissingLastName(), "missing: last name"));
  }
}
