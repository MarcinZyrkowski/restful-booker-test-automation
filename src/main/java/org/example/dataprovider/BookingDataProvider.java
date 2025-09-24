package org.example.dataprovider;

import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.factory.booking.CreateBookingRequestFactory;
import org.junit.jupiter.params.provider.Arguments;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("unused")
public class BookingDataProvider {

  public static Stream<Arguments> missingFieldBookingRequest() {
    return Stream.of(
        Arguments.of(CreateBookingRequestFactory.getWithMissingFirstName(), "missing: first name"),
        Arguments.of(CreateBookingRequestFactory.getWithMissingLastName(), "missing: last name")
    );
  }

}
