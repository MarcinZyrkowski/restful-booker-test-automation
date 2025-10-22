package org.example.factory.booking;

import io.qameta.allure.Step;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.generator.request.BookingGenerator;
import org.example.model.dto.common.Booking;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingFactory {

  @Step("Prepare booking with all valid fields")
  public static Booking getWithAllValidFields() {
    return BookingGenerator.builder().withAllValidFields().build();
  }

  @Step("Prepare booking with missing first name")
  public static Booking getWithMissingFirstName() {
    return BookingGenerator.builder().withAllValidFields().withMissingFirstName().build();
  }

  @Step("Prepare booking with missing last name")
  public static Booking getWithMissingLastName() {
    return BookingGenerator.builder().withAllValidFields().withMissingLastName().build();
  }

  @Step("Prepare booking with valid fields or randomly null fields")
  public static Booking getWithValidFieldsOrRandomlyNullFields() {
    return BookingGenerator.builder().withAllValidFields().withValidFieldsOrRandomlyNull().build();
  }
}
