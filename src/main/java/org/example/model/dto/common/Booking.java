package org.example.model.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.qameta.allure.Step;
import lombok.Builder;
import lombok.With;
import org.example.utils.BookerUtils;

@With
@Builder
@JsonInclude(Include.NON_NULL)
public record Booking(
    @JsonProperty(value = "firstname") String firstName,
    @JsonProperty(value = "lastname") String lastName,
    @JsonProperty(value = "totalprice") Integer totalPrice,
    @JsonProperty(value = "depositpaid") Boolean depositPaid,
    @JsonProperty(value = "bookingdates") BookingDates bookingDates,
    @JsonProperty(value = "additionalneeds") String additionalNeeds) {

  @Step("Merge non-nullable fields from another Booking object")
  public Booking mergeNonNullable(Booking other) {
    if (other == null) {
      return this;
    }

    return Booking.builder()
        .firstName(BookerUtils.nonNullValueOrDefault(other.firstName, this.firstName))
        .lastName(BookerUtils.nonNullValueOrDefault(other.lastName, this.lastName))
        .totalPrice(BookerUtils.nonNullValueOrDefault(other.totalPrice, this.totalPrice))
        .depositPaid(BookerUtils.nonNullValueOrDefault(other.depositPaid, this.depositPaid))
        .bookingDates(this.bookingDates.mergeNonNullable(other.bookingDates))
        .additionalNeeds(
            BookerUtils.nonNullValueOrDefault(other.additionalNeeds, this.additionalNeeds))
        .build();
  }

  @With
  @Builder
  @JsonInclude(Include.NON_NULL)
  public record BookingDates(
      @JsonProperty(value = "checkin") String checkIn,
      @JsonProperty(value = "checkout") String checkOut) {

    public BookingDates mergeNonNullable(BookingDates other) {
      if (other == null) {
        return this;
      }

      return BookingDates.builder()
          .checkIn(BookerUtils.nonNullValueOrDefault(other.checkIn, this.checkIn))
          .checkOut(BookerUtils.nonNullValueOrDefault(other.checkOut, this.checkOut))
          .build();
    }
  }
}
