package org.example.model.dto.request.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record BookingRequestResponse(
    @JsonProperty(value = "firstname") String firstName,
    @JsonProperty(value = "lastname") String lastName,
    @JsonProperty(value = "totalprice") Integer totalPrice,
    @JsonProperty(value = "depositpaid")  Boolean depositPaid,
    @JsonProperty(value = "bookingdates") BookingDates bookingDates,
    @JsonProperty(value = "additionalneeds") String additionalNeeds
) {

  @Builder
  public record BookingDates(
      @JsonProperty(value = "checkin") String checkIn,
      @JsonProperty(value = "checkout") String checkOut
  ) {

  }

}
