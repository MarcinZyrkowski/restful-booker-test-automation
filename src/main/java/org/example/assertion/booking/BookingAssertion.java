package org.example.assertion.booking;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.example.assertion.common.AssertionUtils;
import org.example.assertion.common.ResponseAssertion;
import org.example.helper.merger.BookingMerger;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.common.Booking;
import org.springframework.stereotype.Component;

@Component()
@RequiredArgsConstructor
public class BookingAssertion {

  private final ResponseMapper responseMapper;
  private final ResponseAssertion responseAssertion;
  private final AssertionUtils assertionUtils;
  private final BookingMerger bookingMerger;

  @Step("Assert that booking is equal to expected booking")
  public void assertResponseIsEqualTo(Response response, Booking expected) {
    responseAssertion.assertStatusCodeIsOk(response);
    Booking actual = responseMapper.mapToBookingRequestResponse(response);
    assertionUtils.assertEquals(actual, expected);
  }

  @Step("Assert that booking is partially updated")
  public void assertBookingIsPartiallyUpdated(
      Response response, Booking original, Booking partiallyUpdated) {
    responseAssertion.assertStatusCodeIsOk(response);
    Booking actual = responseMapper.mapToBookingRequestResponse(response);

    Booking expected = bookingMerger.mergeNonNullableBooking(original, partiallyUpdated);
    assertionUtils.assertEquals(actual, expected);
  }
}
