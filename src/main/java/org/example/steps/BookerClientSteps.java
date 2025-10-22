package org.example.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.example.assertion.response.StringResponseAssertion;
import org.example.assertion.response.auth.TokenResponseAssertion;
import org.example.assertion.response.booking.BookingDetailsAssertion;
import org.example.client.BookerClient;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.common.Booking;
import org.example.model.dto.request.auth.User;
import org.example.model.dto.response.auth.Token;
import org.example.model.dto.response.booking.BookingDetails;
import org.example.model.enums.service.StringResponseBody;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookerClientSteps {

  private final BookerClient bookerClient;

  public BookingDetails createBooking(Booking request) {
    Response createResponse = bookerClient.createBooking(request);

    BookingDetailsAssertion.assertThat(createResponse).statusIsOk();

    return ResponseMapper.map(createResponse).toCreateBookingResponse();
  }

  @Step("Fetch booking by ID {bookingId} and assert not found")
  public void fetchBookingAssertNotFound(int bookingId) {
    Response getResponse = bookerClient.getBookingById(bookingId);

    StringResponseAssertion.assertThat(getResponse)
        .statusIsNotFound()
        .body()
        .isEqualTo(StringResponseBody.NOT_FOUND.getBody());
  }

  @Step("Create token for user: {user}")
  public Token createToken(User user) {
    Response tokenResponse = bookerClient.createToken(user);
    TokenResponseAssertion.assertThat(tokenResponse).statusIsOk();

    return ResponseMapper.map(tokenResponse).toTokenResponse();
  }
}
