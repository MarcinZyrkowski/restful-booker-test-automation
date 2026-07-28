package org.example.mapper;

import io.restassured.response.Response;
import java.util.List;
import org.example.model.service.dto.common.Booking;
import org.example.model.service.dto.response.auth.Token;
import org.example.model.service.dto.response.booking.BookingDetails;
import org.example.model.service.dto.response.booking.BookingId;
import org.example.model.service.dto.response.common.ErrorResponse;
import org.springframework.stereotype.Component;

@Component
public class ResponseMapper {

  private <T> T mapTo(Response response, Class<T> clazz) {
    return response.getBody().as(clazz);
  }

  public String mapToStringResponse(Response response) {
    return response.getBody().asString();
  }

  public Token mapToTokenResponse(Response response) {
    return mapTo(response, Token.class);
  }

  public ErrorResponse mapToErrorResponse(Response response) {
    return mapTo(response, ErrorResponse.class);
  }

  public List<BookingId> mapToBookingIdResponseList(Response response) {
    return response.jsonPath().getList("", BookingId.class);
  }

  public BookingDetails mapToCreateBookingResponse(Response response) {
    return mapTo(response, BookingDetails.class);
  }

  public Booking mapToBooking(Response response) {
    return mapTo(response, Booking.class);
  }
}
