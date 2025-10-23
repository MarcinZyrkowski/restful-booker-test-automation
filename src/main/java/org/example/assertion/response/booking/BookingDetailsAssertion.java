package org.example.assertion.response.booking;

import io.qameta.allure.Step;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.example.assertion.AssertionUtils;
import org.example.assertion.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.common.Booking;
import org.example.model.dto.response.booking.BookingDetails;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class BookingDetailsAssertion extends ResponseAssertion<BookingDetailsAssertion> {

  private final ResponseMapper responseMapper;
  private final AssertionUtils assertionUtils;
  private BookingDetails bookingDetails;

  @Step("Extract booking details from response body")
  public BookingDetailsAssertion body() {
    bookingDetails = responseMapper.map(response).toCreateBookingResponse();
    return this;
  }

  @Step("Assert that booking details are created from the given booking request")
  public BookingDetailsAssertion isCreatedFrom(Booking booking) {
    Assertions.assertThat(bookingDetails).isNotNull();
    Assertions.assertThat(bookingDetails.booking()).isNotNull();
    Assertions.assertThat(bookingDetails.bookingId()).isNotNull();

    assertionUtils.assertEquals(bookingDetails.booking(), booking);
    return this;
  }
}
