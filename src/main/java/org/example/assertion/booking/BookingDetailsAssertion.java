package org.example.assertion.booking;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.example.assertion.common.AssertionUtils;
import org.example.assertion.common.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.common.Booking;
import org.example.model.dto.response.booking.BookingDetails;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component()
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class BookingDetailsAssertion extends ResponseAssertion {

  private final ResponseMapper responseMapper;
  private final AssertionUtils assertionUtils;

  @Step("Assert that booking details are created")
  public void assertIsCreated(Response response) {
    assertStatusCodeIsOk(response);
    BookingDetails bookingDetails = responseMapper.mapToCreateBookingResponse(response);
    assertBookingDetailsNotNull(bookingDetails);
  }

  @Step("Assert that booking details are created from the given booking request")
  public void assertIsCreatedFrom(Response response, Booking booking) {
    assertStatusCodeIsOk(response);
    BookingDetails bookingDetails = responseMapper.mapToCreateBookingResponse(response);
    assertBookingDetailsCreatedFrom(bookingDetails, booking);
  }

  private void assertBookingDetailsNotNull(BookingDetails bookingDetails) {
    Assertions.assertThat(bookingDetails).isNotNull();
    Assertions.assertThat(bookingDetails.booking()).isNotNull();
    Assertions.assertThat(bookingDetails.bookingId()).isNotNull();
  }

  private void assertBookingDetailsCreatedFrom(BookingDetails bookingDetails, Booking booking) {
    assertBookingDetailsNotNull(bookingDetails);
    assertionUtils.assertEquals(bookingDetails.booking(), booking);
  }
}
