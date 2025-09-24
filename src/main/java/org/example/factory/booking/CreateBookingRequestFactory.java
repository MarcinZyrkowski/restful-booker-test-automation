package org.example.factory.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.generator.request.CreateBookingRequestGenerator;
import org.example.model.dto.common.BookingRequestResponse;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateBookingRequestFactory {

  public static BookingRequestResponse getWithAllValidFields() {
    return CreateBookingRequestGenerator.builder()
        .withAllValidFields()
        .build();
  }

  public static BookingRequestResponse getWithMissingFirstName() {
    return CreateBookingRequestGenerator.builder()
        .withAllValidFields()
        .withMissingFirstName()
        .build();
  }

  public static BookingRequestResponse getWithMissingLastName() {
    return CreateBookingRequestGenerator.builder()
        .withAllValidFields()
        .withMissingLastName()
        .build();
  }

}
