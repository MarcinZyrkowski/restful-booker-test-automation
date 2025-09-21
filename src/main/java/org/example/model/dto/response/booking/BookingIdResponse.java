package org.example.model.dto.response.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record BookingIdResponse(
    @JsonProperty(value = "bookingid") Integer bookingId
) {

}
