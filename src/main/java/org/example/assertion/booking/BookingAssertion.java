package org.example.assertion.booking;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.example.assertion.common.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.common.Booking;
import org.springframework.stereotype.Component;

@Component("bookingAssertionNG")
@RequiredArgsConstructor
public class BookingAssertion extends ResponseAssertion {

  private final ResponseMapper responseMapper;

  @Step("Assert that booking is equal to expected booking")
  public void assertResponseIsEqualTo(Response response, Booking expected) {
    assertStatusCodeIsOk(response);
    Booking booking = extractBooking(response);
    assertBookingEqualsExpected(booking, expected);
  }

  private Booking extractBooking(Response response) {
    return responseMapper.map(response).toBookingRequestResponse();
  }

  private void assertBookingEqualsExpected(Booking booking, Booking expected) {
    Assertions.assertThat(booking).isEqualTo(expected);
  }
}
