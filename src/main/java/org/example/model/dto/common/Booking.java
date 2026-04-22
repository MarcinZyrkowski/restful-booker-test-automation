package org.example.model.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.With;
import org.example.utils.BookerRandomUtils;

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

  public Booking mergeNonNullable(Booking other) {
    if (other == null) {
      return this;
    }

    return Booking.builder()
        .firstName(BookerRandomUtils.nonNullValueOrDefault(other.firstName, this.firstName))
        .lastName(BookerRandomUtils.nonNullValueOrDefault(other.lastName, this.lastName))
        .totalPrice(BookerRandomUtils.nonNullValueOrDefault(other.totalPrice, this.totalPrice))
        .depositPaid(BookerRandomUtils.nonNullValueOrDefault(other.depositPaid, this.depositPaid))
        .bookingDates(this.bookingDates.mergeNonNullable(other.bookingDates))
        .additionalNeeds(
            BookerRandomUtils.nonNullValueOrDefault(other.additionalNeeds, this.additionalNeeds))
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
          .checkIn(BookerRandomUtils.nonNullValueOrDefault(other.checkIn, this.checkIn))
          .checkOut(BookerRandomUtils.nonNullValueOrDefault(other.checkOut, this.checkOut))
          .build();
    }
  }
}
