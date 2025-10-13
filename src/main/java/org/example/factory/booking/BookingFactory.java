package org.example.factory.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.generator.request.CreateBookingRequestGenerator;
import org.example.model.dto.common.Booking;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingFactory {

  public static Booking getWithAllValidFields() {
    return CreateBookingRequestGenerator.builder()
        .withAllValidFields()
        .build();
  }

  public static Booking getWithMissingFirstName() {
    return CreateBookingRequestGenerator.builder()
        .withAllValidFields()
        .withMissingFirstName()
        .build();
  }

  public static Booking getWithMissingLastName() {
    return CreateBookingRequestGenerator.builder()
        .withAllValidFields()
        .withMissingLastName()
        .build();
  }

}
