package org.example.model.dto.response.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.example.model.dto.common.Booking;

@Builder
public record CreatedBooking(
    @JsonProperty(value = "bookingid") Integer bookingId,
    Booking booking
) {

}
