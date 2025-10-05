package org.example.steps;

import io.restassured.response.Response;
import org.example.assertion.response.StringResponseAssertion;
import org.example.assertion.response.auth.TokenResponseAssertion;
import org.example.assertion.response.booking.CreatedBookingAssertion;
import org.example.client.BookerClient;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.common.Booking;
import org.example.model.dto.request.auth.User;
import org.example.model.dto.response.auth.Token;
import org.example.model.dto.response.booking.CreatedBooking;
import org.example.model.enums.service.StringResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookerClientSteps {

  @Autowired
  private BookerClient bookerClient;

  public CreatedBooking createBooking(Booking request) {
    Response createResponse = bookerClient.createBooking(request);

    CreatedBookingAssertion.assertThat(createResponse)
        .statusIsOk();

    return ResponseMapper.map(createResponse).toCreateBookingResponse();
  }

  public void fetchBookingAssertNotFound(int bookingId) {
    Response getResponse = bookerClient.getBookingById(bookingId);

    StringResponseAssertion.assertThat(getResponse)
        .statusIsNotFound()
        .body()
        .isEqualTo(StringResponseBody.NOT_FOUND.getBody());
  }

  public Token createToken(User user) {
    Response tokenResponse = bookerClient.createToken(user);
    TokenResponseAssertion.assertThat(tokenResponse)
        .statusIsOk();

    return ResponseMapper.map(tokenResponse).toTokenResponse();
  }

}
