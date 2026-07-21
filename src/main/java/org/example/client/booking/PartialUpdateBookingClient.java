package org.example.client.booking;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.example.api.BookerApi;
import org.example.assertion.common.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.service.dto.common.Booking;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartialUpdateBookingClient {

  private final ResponseMapper responseMapper;
  private final BookerApi bookerApi;
  private final ResponseAssertion responseAssertion;

  @Step("Partial update booking with basic auth client call: {bookingId}")
  public Booking partialUpdateBooking(int bookingId, Booking booking) {
    Response response = bookerApi.partialUpdateBooking(bookingId, booking);
    responseAssertion.assertStatusCodeIsOk(response);
    return responseMapper.mapToBookingRequestResponse(response);
  }

  @Step("Partial update booking with token client call: {bookingId}")
  public Booking partialUpdateBooking(int bookingId, Booking booking, String token) {
    Response response = bookerApi.partialUpdateBooking(bookingId, booking, token);
    responseAssertion.assertStatusCodeIsOk(response);
    return responseMapper.mapToBookingRequestResponse(response);
  }

  @Step("Partial update booking expecting error client call: {bookingId}")
  public String partialUpdateBookingExpectingError(int bookingId, Booking booking) {
    Response response = bookerApi.partialUpdateBooking(bookingId, booking);
    responseAssertion.assertStatusCodeIsMethodNotAllowed(response);
    return responseMapper.mapToStringResponse(response);
  }

  @Step("Partial update booking expecting error client call: {bookingId}")
  public String partialUpdateBookingExpectingError(int bookingId, Booking booking, String token) {
    Response response = bookerApi.partialUpdateBooking(bookingId, booking, token);
    responseAssertion.assertStatusCodeIsForbidden(response);
    return responseMapper.mapToStringResponse(response);
  }
}
