package org.example.mapper;

import io.restassured.response.Response;
import java.util.List;
import lombok.NoArgsConstructor;
import org.example.model.dto.common.Booking;
import org.example.model.dto.response.auth.Token;
import org.example.model.dto.response.booking.BookingDetails;
import org.example.model.dto.response.booking.BookingId;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@NoArgsConstructor
public class ResponseMapper {

  private Response response;

  public ResponseMapper map(Response response) {
    this.response = response;
    return this;
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
    return response.jsonPath().getList("", BookingId.class);
  }

  public BookingDetails toCreateBookingResponse() {
    return to(BookingDetails.class);
  }

  public Booking toBookingRequestResponse() {
    return to(Booking.class);
  }
}
