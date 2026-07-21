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
public class UpdateBookingClient {

  private final ResponseMapper responseMapper;
  private final BookerApi bookerApi;
  private final ResponseAssertion responseAssertion;

  @Step("Update booking with basic auth client call: {bookingId}")
  public Booking updateBooking(int bookingId, Booking booking) {
    Response response = bookerApi.updateBooking(bookingId, booking);
    responseAssertion.assertStatusCodeIsOk(response);
    return responseMapper.mapToBookingRequestResponse(response);
  }

  @Step("Update booking with token client call: {bookingId}")
  public Booking updateBooking(int bookingId, Booking booking, String token) {
    Response response = bookerApi.updateBooking(bookingId, booking, token);
    responseAssertion.assertStatusCodeIsOk(response);
    return responseMapper.mapToBookingRequestResponse(response);
  }

  @Step("Update booking expecting error client call: {bookingId}")
  public String updateBookingExpectingError(int bookingId, Booking booking) {
    Response response = bookerApi.updateBooking(bookingId, booking);
    responseAssertion.assertStatusCodeIsMethodNotAllowed(response);
    return responseMapper.mapToStringResponse(response);
  }

  @Step("Update booking expecting error client call: {bookingId}")
  public String updateBookingExpectingError(int bookingId, Booking booking, String token) {
    Response response = bookerApi.updateBooking(bookingId, booking, token);
    responseAssertion.assertStatusCodeIsForbidden(response);
    return responseMapper.mapToStringResponse(response);
  }
}
