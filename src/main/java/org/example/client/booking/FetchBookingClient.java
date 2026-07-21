package org.example.client.booking;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.api.BookerApi;
import org.example.assertion.common.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.service.dto.common.Booking;
import org.example.model.service.dto.response.booking.BookingId;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FetchBookingClient {

  private final ResponseMapper responseMapper;
  private final BookerApi bookerApi;
  private final ResponseAssertion responseAssertion;

  @Step("Get booking by ID client call: {bookingId}")
  public Booking getBookingById(String bookingId) {
    Response response = bookerApi.getBookingById(bookingId);
    responseAssertion.assertStatusCodeIsOk(response);
    return responseMapper.mapToBookingRequestResponse(response);
  }

  @Step("Get booking by ID client call expecting error: {bookingId}")
  public String getBookingByIdExpectingError(String bookingId) {
    Response response = bookerApi.getBookingById(bookingId);
    responseAssertion.assertStatusCodeIsNotFound(response);
    return responseMapper.mapToStringResponse(response);
  }

  @Step("Get booking IDs client call")
  public List<BookingId> getBookingIds(
      String firstName, String lastName, String checkIn, String checkOut) {
    Response response = bookerApi.getBookingIds(firstName, lastName, checkIn, checkOut);
    responseAssertion.assertStatusCodeIsOk(response);
    return responseMapper.mapToBookingIdResponseList(response);
  }
}
