package org.example.booking.update;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.assertion.booking.BookingAssert;
import org.example.assertion.common.StringResponseAssert;
import org.example.client.booking.FetchBookingClient;
import org.example.client.booking.PartialUpdateBookingClient;
import org.example.client.token.TokenClient;
import org.example.config.SpringConfig;
import org.example.factory.booking.BookingFactory;
import org.example.model.service.dto.common.Booking;
import org.example.model.service.dto.request.auth.User;
import org.example.model.service.dto.response.auth.Token;
import org.example.model.service.dto.response.booking.BookingDetails;
import org.example.pool.BookingDetailsPool;
import org.example.tags.Regression;
import org.example.utils.BookerRandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Severity(SeverityLevel.NORMAL)
@Epic("Booking API")
@Feature("Booking Management")
@Story("Partial Update Booking")
@Regression
@SpringBootTest(classes = SpringConfig.class)
@DisplayName("Partial Update Booking")
class PartialUpdateBookingTest {

  @Autowired private BookingDetailsPool bookingDetailsPool;
  @Autowired private BookingFactory bookingFactory;
  @Autowired private PartialUpdateBookingClient partialUpdateBookingClient;
  @Autowired private FetchBookingClient fetchBookingClient;
  @Autowired private TokenClient tokenClient;
  @Autowired private User adminUser;

  @Test
  @DisplayName("Partial update booking with all valid fields - basic auth")
  void partialUpdateBookingUsingBasicAuthTest() {
    // Retrieve an existing booking from the pool or create a new one
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    // Prepare a partial update payload with valid fields
    Booking partialBookingUpdate = bookingFactory.getWithValidOrNullFields();

    // Send request to partially update the booking using basic auth
    Booking response =
        partialUpdateBookingClient.partialUpdateBooking(bookingId, partialBookingUpdate);
    // Verify the response reflects the partial update
    BookingAssert.assertThat(response)
        .isPartiallyUpdated(bookingDetails.booking(), partialBookingUpdate);

    // Fetch the booking and verify the changes persisted
    Booking fetchResponse = fetchBookingClient.getBookingById(String.valueOf(bookingId));
    BookingAssert.assertThat(fetchResponse)
        .isPartiallyUpdated(bookingDetails.booking(), partialBookingUpdate);
  }

  @Test
  @DisplayName("Partial update booking with all valid fields - token auth")
  void partialUpdateBookingUsingTokenTest() {
    // Retrieve an existing booking from the pool or create a new one
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    // Prepare a partial update payload with valid fields
    Booking partialBookingUpdate = bookingFactory.getWithValidOrNullFields();
    // Generate a valid authentication token
    Token tokenResponse = tokenClient.createToken(adminUser);
    String token = tokenResponse.token();

    // Send request to partially update the booking using the token
    Booking response =
        partialUpdateBookingClient.partialUpdateBooking(bookingId, partialBookingUpdate, token);
    // Verify the response reflects the partial update
    BookingAssert.assertThat(response)
        .isPartiallyUpdated(bookingDetails.booking(), partialBookingUpdate);

    // Fetch the booking and verify the changes persisted
    Booking fetchResponse = fetchBookingClient.getBookingById(String.valueOf(bookingId));
    BookingAssert.assertThat(fetchResponse)
        .isPartiallyUpdated(bookingDetails.booking(), partialBookingUpdate);
  }

  @Test
  @DisplayName("Partial update booking with empty body - basic auth")
  void partialUpdateBookingWithEmptyBodyTest() {
    // Retrieve an existing booking from the pool or create a new one
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    // Prepare an empty partial update payload
    Booking emptyBookingUpdate = Booking.builder().build();

    // Send request to partially update the booking with empty payload
    Booking response =
        partialUpdateBookingClient.partialUpdateBooking(bookingId, emptyBookingUpdate);
    // Verify the response reflects the original booking (no changes)
    BookingAssert.assertThat(response).isEqualToBooking(bookingDetails.booking());

    // Fetch the booking and verify no changes were made
    Booking fetchResponse = fetchBookingClient.getBookingById(String.valueOf(bookingId));
    BookingAssert.assertThat(fetchResponse).isEqualToBooking(bookingDetails.booking());

    // Return the booking to the pool
    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Should return: method not allowed when booking ID does not exist")
  void shouldNotPartialUpdateBookingWhenBookingIdDoesNotExistTest() {
    // Generate a random non-existent booking ID
    int nonExistentBookingId = BookerRandomUtils.randomInt(100_000, 200_000);

    // Prepare a partial update payload with valid fields
    Booking partialBookingUpdate = bookingFactory.getWithValidOrNullFields();

    // Attempt to partially update a non-existent booking and expect an error
    String response =
        partialUpdateBookingClient.partialUpdateBookingExpectingError(
            nonExistentBookingId, partialBookingUpdate);

    // Verify a method not allowed status code is returned
    StringResponseAssert.assertThat(response).isMethodNotAllowed();
  }

  @Test
  @DisplayName("Should return: forbidden when updating booking with invalid token")
  void shouldNotPartialUpdateBookingWithInvalidTokenTest() {
    // Retrieve an existing booking from the pool or create a new one
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    // Prepare a partial update payload and an invalid token
    Booking partialBookingUpdate = bookingFactory.getWithValidOrNullFields();
    String invalidToken = "invalid_token";

    // Attempt to partially update the booking with an invalid token and expect an error
    String response =
        partialUpdateBookingClient.partialUpdateBookingExpectingError(
            bookingId, partialBookingUpdate, invalidToken);

    // Verify a forbidden status code is returned
    StringResponseAssert.assertThat(response).isForbidden();

    // Return the booking to the pool
    bookingDetailsPool.push(bookingDetails);
  }
}
