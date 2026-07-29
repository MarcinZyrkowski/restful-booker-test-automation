package org.example.booking.create;

import io.qameta.allure.*;
import java.util.stream.Stream;
import org.example.assertion.booking.BookingDetailsAssert;
import org.example.assertion.common.StringResponseAssert;
import org.example.client.bookingdetails.BookingDetailsClient;
import org.example.config.SpringConfig;
import org.example.dataprovider.BookingDataProvider;
import org.example.factory.booking.BookingFactory;
import org.example.model.service.dto.common.Booking;
import org.example.model.service.dto.response.booking.BookingDetails;
import org.example.pool.BookingDetailsPool;
import org.example.tags.Bug;
import org.example.tags.Regression;
import org.example.tracking.Bugs;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Severity(SeverityLevel.CRITICAL)
@Epic("Booking API")
@Feature("Booking Management")
@Story("Create Booking")
@Regression
@SpringBootTest(classes = SpringConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Create Booking")
class CreateBookingTest {

  @Autowired private BookingFactory bookingFactory;
  @Autowired private BookingDetailsClient bookingDetailsClient;
  @Autowired private BookingDetailsPool bookingDetailsPool;
  @Autowired private BookingDataProvider bookingDataProvider;

  @Test
  @DisplayName("Create booking with all valid fields")
  void createBookingTest() {
    // Prepare a valid booking request payload
    Booking requestBody = bookingFactory.getWithAllValidFields();

    // Send request to create the booking
    BookingDetails bookingDetails = bookingDetailsClient.createBooking(requestBody);

    // Verify the created booking matches the request
    BookingDetailsAssert.assertThat(bookingDetails).isCreatedFrom(requestBody);

    // Add the created booking to the pool for reuse in other tests
    bookingDetailsPool.push(bookingDetails);
  }

  @Issue(value = Bugs.NEGATIVE_TOTAL_PRICE_BUG)
  @Disabled(value = "Skipped because of bug: " + Bugs.NEGATIVE_TOTAL_PRICE_BUG)
  @Bug
  @Test
  @DisplayName("Should not create booking when total price is negative")
  void shouldNotCreateBookingWithNegativeTotalPrice() {
    // Prepare a booking request payload with a negative total price
    Booking requestBody = bookingFactory.getWithNegativeTotalPrice();

    // Attempt to create the booking and expect an error
    String response = bookingDetailsClient.createBookingExpectingError(requestBody);

    // Verify a bad request status code is returned
    StringResponseAssert.assertThat(response).isBadRequest();
  }

  @DisplayName("Should not create booking with missing required field")
  @ParameterizedTest(name = "{1}")
  @MethodSource("providerMissingFieldBookings")
  void shouldNotCreateBookingTest(Booking request, String description) {
    // Attempt to create the booking with missing fields and expect an error
    String response = bookingDetailsClient.createBookingExpectingError(request);

    // Verify an internal server error status code is returned
    StringResponseAssert.assertThat(response).isInternalServerError();
  }

  @DisplayName("Should not create booking with random multiple missing required fields")
  @Test
  void shouldNotCreateBookingWithRandomMissingFieldsTest() {
    // Prepare a booking request payload with random missing required fields
    Booking request = bookingFactory.getWithRandomMissingRequiredFields();

    // Attempt to create the booking and expect an error
    String response = bookingDetailsClient.createBookingExpectingError(request);

    // Verify an internal server error status code is returned
    StringResponseAssert.assertThat(response).isInternalServerError();
  }

  Stream<Arguments> providerMissingFieldBookings() {
    return bookingDataProvider.missingFieldBookings();
  }
}
