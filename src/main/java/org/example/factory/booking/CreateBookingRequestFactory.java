package org.example.factory.booking;

import org.example.generator.request.CreateBookingRequestGenerator;
import org.example.model.dto.common.BookingRequestResponse;
import org.springframework.stereotype.Component;

@Component
public class CreateBookingRequestFactory {

  public BookingRequestResponse getWithAllValidFields() {
    return CreateBookingRequestGenerator.builder()
        .withAllValidFields()
        .build();
  }

}
