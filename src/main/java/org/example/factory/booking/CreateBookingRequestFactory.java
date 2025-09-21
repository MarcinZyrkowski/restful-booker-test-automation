package org.example.factory.booking;

import org.example.generator.request.CreateBookingRequestGenerator;
import org.example.model.dto.request.booking.CreateBookingRequest;
import org.springframework.stereotype.Component;

@Component
public class CreateBookingRequestFactory {

  public CreateBookingRequest getWithOnlyRequiredFields() {
    return CreateBookingRequestGenerator.builder()
        .withAllValidFields()
        .build();
  }

}
