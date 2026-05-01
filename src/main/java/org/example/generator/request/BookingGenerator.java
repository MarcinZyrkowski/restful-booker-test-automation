package org.example.generator.request;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.generator.DateTimesGenerator;
import org.example.model.dto.common.Booking;
import org.example.model.dto.common.Booking.BookingDates;
import org.example.model.enums.utils.AdditionalNeed;
import org.example.utils.BookerRandomUtils;
import org.example.utils.FakerUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingGenerator {

  private Booking booking;

  public static BookingGenerator builder() {
    return new BookingGenerator();
  }

  public Booking build() {
    return booking;
  }

  public BookingGenerator withAllValidFields() {
    long totalPrice = BookerRandomUtils.randomNumber(50, 100);
    String additionalNeed = AdditionalNeed.getRandom().getValue();

    this.booking =
        Booking.builder()
            .firstName(FakerUtils.generateFirstName())
            .lastName(FakerUtils.generateLastName())
            .totalPrice((int) totalPrice)
            .depositPaid(BookerRandomUtils.randomBoolean())
            .bookingDates(validBookingDates())
            .additionalNeeds(additionalNeed)
            .build();
    return this;
  }

  private BookingDates validBookingDates() {
    int maxStayDurationInDays = 50;

    LocalDate checkIn = DateTimesGenerator.getRandomFutureDate();
    LocalDate checkOut = DateTimesGenerator.getRandomDateAfter(checkIn, maxStayDurationInDays);

    return BookingDates.builder().checkIn(checkIn.toString()).checkOut(checkOut.toString()).build();
  }

  public BookingGenerator withMissingFirstName() {
    this.booking = booking.withFirstName(null);
    return this;
  }

  public BookingGenerator withMissingLastName() {
    this.booking = booking.withLastName(null);
    return this;
  }

  public BookingGenerator withMissingTotalPrice() {
    this.booking = booking.withTotalPrice(null);
    return this;
  }

  public BookingGenerator withTotalPrice(Integer totalPrice) {
    this.booking = booking.withTotalPrice(totalPrice);
    return this;
  }

  public BookingGenerator withValidFieldsOrNulls() {
    this.booking =
        Booking.builder()
            .firstName(BookerRandomUtils.randomNullOrValue(booking.firstName()))
            .lastName(BookerRandomUtils.randomNullOrValue(booking.lastName()))
            .totalPrice(BookerRandomUtils.randomNullOrValue(booking.totalPrice()))
            .depositPaid(BookerRandomUtils.randomNullOrValue(booking.depositPaid()))
            .bookingDates(BookerRandomUtils.randomNullOrValue(validBookingDates()))
            .additionalNeeds(
                BookerRandomUtils.randomNullOrValue(AdditionalNeed.getRandom().getValue()))
            .build();
    return this;
  }

  public BookingGenerator withMissingDepositPaid() {
    this.booking = booking.withDepositPaid(null);
    return this;
  }

  public BookingGenerator withMissingBookingDates() {
    this.booking = booking.withBookingDates(null);
    return this;
  }

  public BookingGenerator withMissingCheckin() {
    this.booking = booking.withBookingDates(booking.bookingDates().withCheckIn(null));
    return this;
  }

  public BookingGenerator withMissingCheckout() {
    this.booking = booking.withBookingDates(booking.bookingDates().withCheckOut(null));
    return this;
  }
}
