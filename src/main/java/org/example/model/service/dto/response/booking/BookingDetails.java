package org.example.model.service.dto.response.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.example.model.service.dto.common.Booking;

@Builder
public record BookingDetails(
    @JsonProperty(value = "bookingid") Integer bookingId, Booking booking) {}
