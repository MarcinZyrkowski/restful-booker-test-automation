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
    // Retrieve an existing booking from the pool or create a new one
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    String bookingId = String.valueOf(bookingDetails.bookingId());

    // Send request to fetch the booking by its ID
    Booking actualBooking = fetchBookingClient.getBookingById(bookingId);
    // Verify the fetched booking matches the pooled booking details
    BookingAssert.assertThat(actualBooking).isEqualToBooking(bookingDetails.booking());

    // Return the booking to the pool
    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Fetch booking by id that doesn't exist")
  void fetchBookingByIdThatNotExistsTest() {
    // Generate a random non-existent booking ID
    String nonExistentBookingId = BookerRandomUtils.randomLongAsString(100_000, 200_000);

    // Attempt to fetch the non-existent booking and expect an error
    String response = fetchBookingClient.getBookingByIdExpectingError(nonExistentBookingId);

    // Verify a not found status code is returned
    StringResponseAssert.assertThat(response).isNotFound();
  }

  @Test
  @DisplayName("Fetch booking by id with random alphanumeric string sequence")
  void fetchBookingWithRandomAlphanumericIdTest() {
    // Generate a random alphanumeric string to use as an invalid ID
    String randomId = BookerStringUtils.randomAlphaNumericSequence();

    // Attempt to fetch using the invalid ID and expect an error
    String response = fetchBookingClient.getBookingByIdExpectingError(randomId);

    // Verify a not found status code is returned
    StringResponseAssert.assertThat(response).isNotFound();
  }
}
