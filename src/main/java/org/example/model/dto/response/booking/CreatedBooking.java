package org.example.model.dto.response.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.model.dto.common.Booking;

public record CreatedBooking(
    @JsonProperty(value = "bookingid") Integer bookingId,
    Booking booking
) {

}
