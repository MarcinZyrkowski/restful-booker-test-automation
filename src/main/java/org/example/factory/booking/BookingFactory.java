package org.example.factory.booking;

import io.qameta.allure.Step;
import org.example.generator.request.BookingGenerator;
import org.example.model.dto.common.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingFactory {

  @Step("Prepare booking with all valid fields")
  public Booking getWithAllValidFields() {
    return BookingGenerator.builder().withAllValidFields().build();
  }

  @Step("Prepare booking with missing first name")
  public Booking getWithMissingFirstName() {
    return BookingGenerator.builder().withAllValidFields().withMissingFirstName().build();
  }

  @Step("Prepare booking with missing last name")
  public Booking getWithMissingLastName() {
    return BookingGenerator.builder().withAllValidFields().withMissingLastName().build();
  }

  @Step("Prepare booking with valid fields or randomly null fields")
  public Booking getWithValidFieldsOrRandomlyNullFields() {
    return BookingGenerator.builder().withAllValidFields().withValidFieldsOrRandomlyNull().build();
  }
}
