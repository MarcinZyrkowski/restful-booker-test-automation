package org.example.booking.fetch;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.assertion.booking.BookingAssert;
import org.example.assertion.common.StringResponseAssert;
import org.example.client.booking.FetchBookingClient;
import org.example.config.SpringConfig;
import org.example.model.service.dto.common.Booking;
import org.example.model.service.dto.response.booking.BookingDetails;
import org.example.pool.BookingDetailsPool;
import org.example.tags.Regression;
import org.example.utils.BookerRandomUtils;
import org.example.utils.BookerStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Severity(SeverityLevel.NORMAL)
@Epic("Booking API")
@Feature("Booking Management")
@Story("Fetch Booking")
@Regression
@SpringBootTest(classes = SpringConfig.class)
@DisplayName("Fetch Booking by Id")
class FetchBookingTest {

  @Autowired private BookingDetailsPool bookingDetailsPool;
  @Autowired private FetchBookingClient fetchBookingClient;

  @Test
  @DisplayName("Fetch booking by id")
  void fetchBookingTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    String bookingId = String.valueOf(bookingDetails.bookingId());

    Booking actualBooking = fetchBookingClient.getBookingById(bookingId);
    BookingAssert.assertThat(actualBooking).isEqualToBooking(bookingDetails.booking());

    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Fetch booking by id that doesn't exist")
  void fetchBookingByIdThatNotExistsTest() {
    String nonExistentBookingId = BookerRandomUtils.randomLongAsString(100_000, 200_000);

    String response = fetchBookingClient.getBookingByIdExpectingError(nonExistentBookingId);

    StringResponseAssert.assertThat(response).isNotFound();
  }

  @Test
  @DisplayName("Fetch booking by id with random alphanumeric string sequence")
  void fetchBookingWithRandomAlphanumericIdTest() {
    String randomId = BookerStringUtils.randomAlphaNumericSequence();

    String response = fetchBookingClient.getBookingByIdExpectingError(randomId);

    StringResponseAssert.assertThat(response).isNotFound();
  }
}
