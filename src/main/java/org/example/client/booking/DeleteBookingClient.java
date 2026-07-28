package org.example.client.booking;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.example.api.BookerApi;
import org.example.assertion.common.ResponseAssert;
import org.example.mapper.ResponseMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteBookingClient {

  private final ResponseMapper responseMapper;
  private final BookerApi bookerApi;

  @Step("Delete booking with basic auth client call: {bookingId}")
  public String deleteBooking(int bookingId) {
    Response response = bookerApi.deleteBooking(bookingId);
    // The API idiosyncratically returns 201 Created for a successful DELETE instead of 200 or 204
    ResponseAssert.assertThat(response).isCreated();
    return responseMapper.mapToStringResponse(response);
  }

  @Step("Delete booking with token client call: {bookingId}")
  public String deleteBooking(int bookingId, String token) {
    Response response = bookerApi.deleteBooking(bookingId, token);
    // The API idiosyncratically returns 201 Created for a successful DELETE instead of 200 or 204
    ResponseAssert.assertThat(response).isCreated();
    return responseMapper.mapToStringResponse(response);
  }

  @Step("Delete booking expecting error client call: {bookingId}")
  public String deleteBookingExpectingError(int bookingId, String token) {
    Response response = bookerApi.deleteBooking(bookingId, token);
    ResponseAssert.assertThat(response).isForbidden();
    return responseMapper.mapToStringResponse(response);
  }
}
