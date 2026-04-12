package org.example.assertionNG.booking;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.example.assertionNG.common.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.response.booking.BookingId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingIdAssertion extends ResponseAssertion {

  private final ResponseMapper responseMapper;

  @Step("Assert that booking IDs list is not null and not empty")
  public void assertHasBookingIds(Response response) {
    assertStatusCodeIsOk(response);
    List<BookingId> bookingIdList = extractBookingIdList(response);
    assertBookingIdsNotEmpty(bookingIdList);
  }

  @Step("Assert that booking IDs list contains booking ID: {bookingId}")
  public void assertHasBookingId(Response response, int bookingId) {
    assertStatusCodeIsOk(response);
    List<BookingId> bookingIdList = extractBookingIdList(response);
    assertBookingIdExists(bookingIdList, bookingId);
  }

  private List<BookingId> extractBookingIdList(Response response) {
    return responseMapper.map(response).toBookingIdResponseList();
  }

  private void assertBookingIdsNotEmpty(List<BookingId> bookingIdList) {
    Assertions.assertThat(bookingIdList).isNotNull().isNotEmpty();
  }

  private void assertBookingIdExists(List<BookingId> bookingIdList, int bookingId) {
    Assertions.assertThat(bookingIdList).isNotNull().isNotEmpty();
    Assertions.assertThat(bookingIdList)
        .anyMatch(b -> b.bookingId() != null && b.bookingId() == bookingId);
  }
}

