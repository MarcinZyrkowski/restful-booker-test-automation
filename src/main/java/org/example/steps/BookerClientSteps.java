package org.example.steps;

import io.restassured.response.Response;
import org.example.assertion.response.StringResponseAssertion;
import org.example.assertion.response.booking.CreateBookingResponseAssertion;
import org.example.client.BookerClient;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.common.BookingRequestResponse;
import org.example.model.dto.request.auth.UserRequest;
import org.example.model.dto.response.auth.TokenResponse;
import org.example.model.dto.response.booking.CreateBookingResponse;
import org.example.model.enums.service.StringResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookerClientSteps {

  @Autowired
  private BookerClient bookerClient;

  public CreateBookingResponse createBooking(BookingRequestResponse request) {
    Response createResponse = bookerClient.createBooking(request);

    CreateBookingResponseAssertion.assertThat(createResponse)
        .statusIsOk();

    return ResponseMapper.map(createResponse).toCreateBookingResponse();
  }

  public void verifyBookingNotFound(int bookingId) {
    Response getResponse = bookerClient.getBookingById(bookingId);

    StringResponseAssertion.assertThat(getResponse)
        .statusIsNotFound()
        .body()
        .isEqualTo(StringResponseBody.NOT_FOUND.getBody());
  }

  public TokenResponse createToken(UserRequest userRequest) {
    Response tokenResponse = bookerClient.createToken(userRequest);
    return ResponseMapper.map(tokenResponse).toTokenResponse();
  }

}
