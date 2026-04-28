package org.example.booking.fetch;

import io.restassured.response.Response;
import org.example.SpringTestContext;
import org.example.model.dto.response.booking.BookingDetails;
import org.example.utils.BookerRandomUtils;
import org.example.utils.BookerStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Fetch Booking by Id")
class FetchBookingTest extends SpringTestContext {

  @Test
  @DisplayName("Fetch booking by id")
  void fetchBookingTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    String bookingId = String.valueOf(bookingDetails.bookingId());

    Response fetchResponse = bookerClient.getBookingById(bookingId);

    bookingAssertion.assertResponseIsEqualTo(fetchResponse, bookingDetails.booking());

    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Fetch booking by id that doesn't exist")
  void fetchBookingByIdThatNotExistsTest() {
    String nonExistentBookingId = BookerRandomUtils.randomNumberAsString(100_000, 200_000);

    Response fetchResponse = bookerClient.getBookingById(nonExistentBookingId);

    stringResponseAssertion.assertResponseIsNotFound(fetchResponse);
  }

  @Test
  @DisplayName("Fetch booking by id with random alphanumeric string sequence")
  void fetchBookingWithRandomAlphanumericIdTest() {
    String randomId = BookerStringUtils.randomAlphaNumericSequence();

    Response fetchResponse = bookerClient.getBookingById(randomId);

    stringResponseAssertion.assertResponseIsNotFound(fetchResponse);
  }
}
