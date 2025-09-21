package org.example.generator.request;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.example.generator.DateTimesGenerator;
import org.example.model.dto.request.booking.CreateBookingRequest;
import org.example.model.dto.request.booking.CreateBookingRequest.BookingDates;
import org.example.model.enums.utils.AdditionalNeed;
import org.example.utils.BookerRandomUtils;
import org.example.utils.FakerUtils;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateBookingRequestGenerator {

  private CreateBookingRequest createBookingRequest;

  public static CreateBookingRequestGenerator builder() {
    return new CreateBookingRequestGenerator(null);
  }

  public CreateBookingRequest build() {
    return createBookingRequest;
  }

  public CreateBookingRequestGenerator withAllValidFields() {
    int totalPrice = BookerRandomUtils.RANDOM.randomInt(50, 500);
    String additionalNeed = AdditionalNeed.getRandom().getValue();

    this.createBookingRequest = CreateBookingRequest.builder()
        .firstName(FakerUtils.getFAKER().name().firstName())
        .lastName(FakerUtils.getFAKER().name().lastName())
        .totalPrice(totalPrice)
        .depositPaid(BookerRandomUtils.RANDOM.randomBoolean())
        .bookingDates(validBookingDates())
        .additionalNeeds(additionalNeed)
        .build();
    return this;
  }

  private BookingDates validBookingDates() {
    int maxStayDurationInDays = 50;

    LocalDate checkIn = DateTimesGenerator.getRandomFutureDate();
    LocalDate checkOut = DateTimesGenerator.getRandomDateAfter(checkIn, maxStayDurationInDays);

    return BookingDates.builder()
        .checkIn(checkIn.toString())
        .checkOut(checkOut.toString())
        .build();
  }

}
