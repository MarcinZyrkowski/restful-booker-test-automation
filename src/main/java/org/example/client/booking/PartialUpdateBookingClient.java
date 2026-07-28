package org.example.client.booking;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.example.api.BookerApi;
import org.example.assertion.common.ResponseAssert;
import org.example.mapper.ResponseMapper;
import org.example.model.service.dto.common.Booking;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartialUpdateBookingClient {

  private final ResponseMapper responseMapper;
  private final BookerApi bookerApi;

  @Step("Partial update booking with basic auth client call: {bookingId}")
  public Booking partialUpdateBooking(int bookingId, Booking booking) {
    Response response = bookerApi.partialUpdateBooking(bookingId, booking);
    ResponseAssert.assertThat(response).isOk();
    return responseMapper.mapToBooking(response);
  }

  @Step("Partial update booking with token client call: {bookingId}")
  public Booking partialUpdateBooking(int bookingId, Booking booking, String token) {
    Response response = bookerApi.partialUpdateBooking(bookingId, booking, token);
    ResponseAssert.assertThat(response).isOk();
    return responseMapper.mapToBooking(response);
  }

  @Step("Partial update booking expecting error client call: {bookingId}")
  public String partialUpdateBookingExpectingError(int bookingId, Booking booking) {
    Response response = bookerApi.partialUpdateBooking(bookingId, booking);
    ResponseAssert.assertThat(response).isMethodNotAllowed();
    return responseMapper.mapToStringResponse(response);
  }

  @Step("Partial update booking expecting error client call: {bookingId}")
  public String partialUpdateBookingExpectingError(int bookingId, Booking booking, String token) {
    Response response = bookerApi.partialUpdateBooking(bookingId, booking, token);
    ResponseAssert.assertThat(response).isForbidden();
    return responseMapper.mapToStringResponse(response);
  }
}
