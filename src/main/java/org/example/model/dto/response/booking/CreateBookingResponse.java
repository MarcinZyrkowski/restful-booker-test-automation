package org.example.model.dto.response.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.model.dto.common.BookingRequestResponse;

public record CreateBookingResponse(
    @JsonProperty(value = "bookingid") Integer bookingId,
    BookingRequestResponse booking
) {

}
