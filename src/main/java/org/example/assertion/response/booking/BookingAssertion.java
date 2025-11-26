package org.example.assertion.response.booking;

import io.qameta.allure.Step;
import lombok.RequiredArgsConstructor;
import org.example.assertion.AssertionUtils;
import org.example.assertion.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.common.Booking;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class BookingAssertion extends ResponseAssertion<BookingAssertion> {

  private final ResponseMapper responseMapper;
  private final AssertionUtils assertionUtils;
  private Booking booking;

  @Step("Extract booking from response body")
  public BookingAssertion body() {
    booking = responseMapper.map(response).toBookingRequestResponse();
    return this;
  }

  @Step("Assert that booking is equal to expected booking")
  public BookingAssertion isEqualTo(Booking expected) {

    assertionUtils.assertEquals(booking, expected);
    return this;
  }
}
