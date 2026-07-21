package org.example.client.booking;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.example.api.BookerApi;
import org.example.assertion.common.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteBookingClient {

  private final ResponseMapper responseMapper;
  private final BookerApi bookerApi;
  private final ResponseAssertion responseAssertion;

  @Step("Delete booking with basic auth client call: {bookingId}")
  public String deleteBooking(int bookingId) {
    Response response = bookerApi.deleteBooking(bookingId);
    responseAssertion.assertStatusCodeIsCreated(response);
    return responseMapper.mapToStringResponse(response);
  }

  @Step("Delete booking with token client call: {bookingId}")
  public String deleteBooking(int bookingId, String token) {
    Response response = bookerApi.deleteBooking(bookingId, token);
    responseAssertion.assertStatusCodeIsCreated(response);
    return responseMapper.mapToStringResponse(response);
  }

  @Step("Delete booking expecting error client call: {bookingId}")
  public String deleteBookingExpectingError(int bookingId, String token) {
    Response response = bookerApi.deleteBooking(bookingId, token);
    responseAssertion.assertStatusCodeIsForbidden(response);
    return responseMapper.mapToStringResponse(response);
  }
}
