package org.example.client.bookingdetails;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.example.api.BookerApi;
import org.example.assertion.common.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.service.dto.common.Booking;
import org.example.model.service.dto.response.booking.BookingDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingDetailsClient {

  private final ResponseMapper responseMapper;
  private final BookerApi bookerApi;
  private final ResponseAssertion responseAssertion;

  @Step("Create booking client call")
  public BookingDetails createBooking(Booking request) {
    Response response = bookerApi.createBooking(request);
    responseAssertion.assertStatusCodeIsOk(response);
    return responseMapper.mapToCreateBookingResponse(response);
  }

  @Step("Create booking expecting error client call")
  public String createBookingExpectingError(Booking request) {
    Response response = bookerApi.createBooking(request);
    int statusCode = response.getStatusCode();
    Assertions.assertThat(statusCode)
        .isIn(HttpStatus.SC_BAD_REQUEST, HttpStatus.SC_INTERNAL_SERVER_ERROR);
    return responseMapper.mapToStringResponse(response);
  }
}
