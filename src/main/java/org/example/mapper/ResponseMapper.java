package org.example.mapper;

import io.restassured.response.Response;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.common.Booking;
import org.example.model.dto.response.auth.Token;
import org.example.model.dto.response.booking.BookingId;
import org.example.model.dto.response.booking.CreatedBooking;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseMapper {

  private final Response response;

  public static ResponseMapper map(Response response) {
    return new ResponseMapper(response);
  }

  private <T> T to(Class<T> clazz) {
    return response.getBody().as(clazz);
  }

  public String toStringResponse() {
    return response.getBody().asString();
  }

  public Token toTokenResponse() {
    return to(Token.class);
  }

  public List<BookingId> toBookingIdResponseList() {
    return response.jsonPath()
        .getList("", BookingId.class);
  }

  public CreatedBooking toCreateBookingResponse() {
    return to(CreatedBooking.class);
  }

  public Booking toBookingRequestResponse() {
    return to(Booking.class);
  }

}
