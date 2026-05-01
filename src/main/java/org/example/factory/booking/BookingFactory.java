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
  public Booking getWithValidOrNullFields() {
    return BookingGenerator.builder().withAllValidFields().withValidFieldsOrNulls().build();
  }

  @Step("Prepare booking with missing total price")
  public Booking getWithMissingTotalPrice() {
    return BookingGenerator.builder().withAllValidFields().withMissingTotalPrice().build();
  }

  @Step("Prepare booking with missing deposit paid")
  public Booking getWithMissingDepositPaid() {
    return BookingGenerator.builder().withAllValidFields().withMissingDepositPaid().build();
  }

  @Step("Prepare booking with missing booking dates")
  public Booking getWithMissingBookingDates() {
    return BookingGenerator.builder().withAllValidFields().withMissingBookingDates().build();
  }

  @Step("Prepare booking with missing checkin date")
  public Booking getWithMissingCheckin() {
    return BookingGenerator.builder().withAllValidFields().withMissingCheckin().build();
  }

  @Step("Prepare booking with missing checkout date")
  public Booking getWithMissingCheckout() {
    return BookingGenerator.builder().withAllValidFields().withMissingCheckout().build();
  }

  @Step("Prepare booking with negative total price")
  public Booking getWithNegativeTotalPrice() {
    return BookingGenerator.builder().withAllValidFields().withTotalPrice(-100).build();
  }
}
