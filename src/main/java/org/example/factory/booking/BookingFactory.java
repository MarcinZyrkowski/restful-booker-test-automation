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

  @Step("Prepare booking with missing total price")
  public Booking getWithMissingTotalPrice() {
    return getWithAllValidFields().withTotalPrice(null);
  }

  @Step("Prepare booking with missing deposit paid")
  public Booking getWithMissingDepositPaid() {
    return getWithAllValidFields().withDepositPaid(null);
  }

  @Step("Prepare booking with missing booking dates")
  public Booking getWithMissingBookingDates() {
    return getWithAllValidFields().withBookingDates(null);
  }

  @Step("Prepare booking with missing checkin date")
  public Booking getWithMissingCheckin() {
    Booking booking = getWithAllValidFields();
    return booking.withBookingDates(booking.bookingDates().withCheckIn(null));
  }

  @Step("Prepare booking with missing checkout date")
  public Booking getWithMissingCheckout() {
    Booking booking = getWithAllValidFields();
    return booking.withBookingDates(booking.bookingDates().withCheckOut(null));
  }
}
