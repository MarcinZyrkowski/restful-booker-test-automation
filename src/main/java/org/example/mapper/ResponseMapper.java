package org.example.mapper;

import io.restassured.response.Response;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.common.BookingRequestResponse;
import org.example.model.dto.response.booking.BookingIdResponse;
import org.example.model.dto.response.auth.TokenResponse;
import org.example.model.dto.response.booking.CreateBookingResponse;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseMapper {

  private final Response response;

  public static ResponseMapper map(Response response) {
    return new ResponseMapper(response);
  }

  private  <T> T to(Class<T> clazz) {
    return response.getBody().as(clazz);
  }

  public String toStringResponse() {
    return response.getBody().asString();
  }

  public TokenResponse toTokenResponse() {
    return to(TokenResponse.class);
  }

  public List<BookingIdResponse> toBookingIdResponseList() {
    return response.jsonPath()
        .getList("", BookingIdResponse.class);
  }

  public CreateBookingResponse toCreateBookingResponse() {
    return to(CreateBookingResponse.class);
  }

  public BookingRequestResponse toBookingRequestResponse() {
    return to(BookingRequestResponse.class);
  }

}
