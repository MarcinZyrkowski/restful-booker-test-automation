package org.example.factory.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.generator.request.BookingGenerator;
import org.example.model.dto.common.Booking;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingFactory {

  public static Booking getWithAllValidFields() {
    return BookingGenerator.builder()
        .withAllValidFields()
        .build();
  }

  public static Booking getWithMissingFirstName() {
    return BookingGenerator.builder()
        .withAllValidFields()
        .withMissingFirstName()
        .build();
  }

  public static Booking getWithMissingLastName() {
    return BookingGenerator.builder()
        .withAllValidFields()
        .withMissingLastName()
        .build();
  }

  public static Booking getWithValidFieldsOrRandomlyNullFields() {
    return BookingGenerator.builder()
        .withAllValidFields()
        .withValidFieldsOrRandomlyNull()
        .build();
  }

}
