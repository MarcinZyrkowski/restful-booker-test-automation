package org.example.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.example.assertion.common.ResponseAssertion;
import org.example.assertion.common.StringResponseAssertion;
import org.example.client.BookerClient;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.common.Booking;
import org.example.model.dto.request.auth.User;
import org.example.model.dto.response.auth.Token;
import org.example.model.dto.response.booking.BookingDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookerClientSteps {

  private final ResponseMapper responseMapper;
  private final BookerClient bookerClient;
  private final StringResponseAssertion stringResponseAssertion;
  private final ResponseAssertion responseAssertion;

  @Step("Create booking")
  public BookingDetails createBooking(Booking request) {
    Response createResponse = bookerClient.createBooking(request);
    responseAssertion.assertStatusCodeIsOk(createResponse);

    return responseMapper.mapToCreateBookingResponse(createResponse);
  }

  @Step("Fetch booking by ID {bookingId} and assert not found")
  public void fetchBookingAssertNotFound(int bookingId) {
    Response getResponse = bookerClient.getBookingById(bookingId);

    stringResponseAssertion.assertResponseIsNotFound(getResponse);
  }

  @Step("Create token")
  public Token createToken(User user) {
    Response tokenResponse = bookerClient.createToken(user);
    responseAssertion.assertStatusCodeIsOk(tokenResponse);

    return responseMapper.mapToTokenResponse(tokenResponse);
  }
}
