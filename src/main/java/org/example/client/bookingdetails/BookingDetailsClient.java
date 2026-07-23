package org.example.client.bookingdetails;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.example.api.BookerApi;
import org.example.assertion.common.ResponseAssert;
import org.example.mapper.ResponseMapper;
import org.example.model.service.dto.common.Booking;
import org.example.model.service.dto.response.booking.BookingDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingDetailsClient {

  private final ResponseMapper responseMapper;
  private final BookerApi bookerApi;

  @Step("Create booking client call")
  public BookingDetails createBooking(Booking request) {
    Response response = bookerApi.createBooking(request);
    ResponseAssert.assertThat(response).isOk();
    return responseMapper.mapToCreateBookingResponse(response);
  }

  @Step("Create booking expecting error client call")
  public String createBookingExpectingError(Booking request) {
    Response response = bookerApi.createBooking(request);
    ResponseAssert.assertThat(response).isBadRequestOrInternalServerError();
    return responseMapper.mapToStringResponse(response);
  }
}
