package org.example.booking.update;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.assertion.booking.BookingAssert;
import org.example.assertion.common.StringResponseAssert;
import org.example.client.booking.FetchBookingClient;
import org.example.client.booking.UpdateBookingClient;
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

@Severity(SeverityLevel.CRITICAL)
@Epic("Booking API")
@Feature("Booking Management")
@Story("Update Booking")
@Regression
@SpringBootTest(classes = SpringConfig.class)
@DisplayName("Update Booking")
class UpdateBookingTest {

  @Autowired private BookingDetailsPool bookingDetailsPool;
  @Autowired private BookingFactory bookingFactory;
  @Autowired private UpdateBookingClient updateBookingClient;
  @Autowired private FetchBookingClient fetchBookingClient;
  @Autowired private TokenClient tokenClient;
  @Autowired private User adminUser;

  @Test
  @DisplayName("Update booking with all valid fields - basic auth")
  void updateBookingUsingBasicAuthTest() {
    // Retrieve an existing booking from the pool or create a new one
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    // Prepare a full update payload with valid fields
    Booking bookingUpdate = bookingFactory.getWithAllValidFields();

    // Send request to fully update the booking using basic auth
    Booking response = updateBookingClient.updateBooking(bookingId, bookingUpdate);
    // Verify the response matches the updated payload
    BookingAssert.assertThat(response).isEqualToBooking(bookingUpdate);

    // Fetch the booking and verify the changes persisted
    Booking fetchedResponse = fetchBookingClient.getBookingById(String.valueOf(bookingId));
    BookingAssert.assertThat(fetchedResponse).isEqualToBooking(bookingUpdate);

    // Add the updated booking to the pool for reuse
    bookingDetailsPool.push(
        BookingDetails.builder().bookingId(bookingId).booking(bookingUpdate).build());
  }

  @Test
  @DisplayName("Update booking with all valid fields - token auth")
  void updateBookingUsingTokenTest() {
    // Retrieve an existing booking from the pool or create a new one
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    // Prepare a full update payload with valid fields
    Booking bookingUpdate = bookingFactory.getWithAllValidFields();

    // Generate a valid authentication token
    Token tokenResponse = tokenClient.createToken(adminUser);
    String token = tokenResponse.token();

    // Send request to fully update the booking using the token
    Booking response = updateBookingClient.updateBooking(bookingId, bookingUpdate, token);
    // Verify the response matches the updated payload
    BookingAssert.assertThat(response).isEqualToBooking(bookingUpdate);

    // Fetch the booking and verify the changes persisted
    Booking fetchedResponse = fetchBookingClient.getBookingById(String.valueOf(bookingId));
    BookingAssert.assertThat(fetchedResponse).isEqualToBooking(bookingUpdate);

    // Add the updated booking to the pool for reuse
    bookingDetailsPool.push(
        BookingDetails.builder().bookingId(bookingId).booking(bookingUpdate).build());
  }

  @Test
  @DisplayName("Should return: forbidden when updating booking with invalid token")
  void shouldNotUpdateBookingWithInvalidTokenTest() {
    // Retrieve an existing booking from the pool or create a new one
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    // Prepare a full update payload and an invalid token
    Booking bookingUpdate = bookingFactory.getWithAllValidFields();
    String invalidToken = "invalid_token";

    // Attempt to fully update the booking with an invalid token and expect an error
    String response =
        updateBookingClient.updateBookingExpectingError(bookingId, bookingUpdate, invalidToken);

    // Verify a forbidden status code is returned
    StringResponseAssert.assertThat(response).isForbidden();

    // Return the original booking to the pool
    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Should return: method not allowed when booking ID does not exist")
  void shouldNotUpdateBookingWhenBookingIdDoesNotExistTest() {
    // Generate a random non-existent booking ID
    int nonExistentBookingId = BookerRandomUtils.randomInt(100_000, 200_000);

    // Prepare a full update payload
    Booking bookingUpdate = bookingFactory.getWithAllValidFields();

    // Attempt to fully update a non-existent booking and expect an error
    String response =
        updateBookingClient.updateBookingExpectingError(nonExistentBookingId, bookingUpdate);

    // Verify a method not allowed status code is returned
    StringResponseAssert.assertThat(response).isMethodNotAllowed();
  }
}
