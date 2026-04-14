package org.example.assertion.booking;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.example.assertion.common.AssertionUtils;
import org.example.assertion.common.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.response.booking.BookingId;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingIdAssertion {

  private final ResponseMapper responseMapper;
  private final ResponseAssertion responseAssertion;
  private final AssertionUtils assertionUtils;

  @Step("Assert that booking IDs list is not null and not empty")
  public void assertBookingIdsAreNotEmpty(Response response) {
    responseAssertion.assertStatusCodeIsOk(response);
    List<BookingId> bookingIdList = responseMapper.mapToBookingIdResponseList(response);
    assertBookingIdsNotEmpty(bookingIdList);
  }

  @Step("Assert that booking IDs list contains booking ID: {bookingId}")
  public void assertBookingIdsContainsBookingId(Response response, int bookingId) {
    responseAssertion.assertStatusCodeIsOk(response);
    List<BookingId> bookingIdList = responseMapper.mapToBookingIdResponseList(response);
    assertBookingIdExists(bookingIdList, bookingId);
  }

  private void assertBookingIdsNotEmpty(List<BookingId> bookingIdList) {
    Assertions.assertThat(bookingIdList).isNotNull().isNotEmpty();
  }

  private void assertBookingIdExists(List<BookingId> bookingIdList, int bookingId) {
    Assertions.assertThat(bookingIdList).isNotNull().isNotEmpty();
    assertionUtils.assertElementIsInCollection(new BookingId(bookingId), bookingIdList);
  }
}
