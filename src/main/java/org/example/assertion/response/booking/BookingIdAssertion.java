package org.example.assertion.response.booking;

import io.qameta.allure.Step;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.example.assertion.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.response.booking.BookingId;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class BookingIdAssertion extends ResponseAssertion<BookingIdAssertion> {

  private final ResponseMapper responseMapper;
  private List<BookingId> bookingIdList;

  @Step("Extract booking IDs from response body")
  public BookingIdAssertion body() {
    bookingIdList = responseMapper.map(response).toBookingIdResponseList();
    return this;
  }

  @Step("Assert that booking IDs list is not null and not empty")
  public BookingIdAssertion hasBookingIds() {
    Assertions.assertThat(bookingIdList).isNotNull().isNotEmpty();
    return this;
  }
}
