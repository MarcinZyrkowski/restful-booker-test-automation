package org.example.model.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.With;

@With
@Builder(toBuilder = true)
@JsonInclude(Include.NON_NULL)
public record Booking(
    @JsonProperty(value = "firstname") String firstName,
    @JsonProperty(value = "lastname") String lastName,
    @JsonProperty(value = "totalprice") Integer totalPrice,
    @JsonProperty(value = "depositpaid") Boolean depositPaid,
    @JsonProperty(value = "bookingdates") BookingDates bookingDates,
    @JsonProperty(value = "additionalneeds") String additionalNeeds) {

  @With
  @Builder(toBuilder = true)
  @JsonInclude(Include.NON_NULL)
  public record BookingDates(
      @JsonProperty(value = "checkin") String checkIn,
      @JsonProperty(value = "checkout") String checkOut) {}
}
