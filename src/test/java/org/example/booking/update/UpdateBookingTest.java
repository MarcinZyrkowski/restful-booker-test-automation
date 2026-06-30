package org.example.booking.update;

import io.restassured.response.Response;
import org.example.assertion.booking.BookingAssertion;
import org.example.assertion.common.StringResponseAssertion;
import org.example.client.BookerClient;
import org.example.config.SpringConfig;
import org.example.factory.booking.BookingFactory;
import org.example.model.service.dto.common.Booking;
import org.example.model.service.dto.request.auth.User;
import org.example.model.service.dto.response.booking.BookingDetails;
import org.example.pool.BookingDetailsPool;
import org.example.steps.BookerClientSteps;
import org.example.tags.Regression;
import org.example.utils.BookerRandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Regression
@SpringBootTest(classes = SpringConfig.class)
@DisplayName("Update Booking")
class UpdateBookingTest {

  @Autowired private BookingDetailsPool bookingDetailsPool;
  @Autowired private BookingFactory bookingFactory;
  @Autowired private BookerClient bookerClient;
  @Autowired private BookingAssertion bookingAssertion;
  @Autowired private BookerClientSteps bookerClientSteps;
  @Autowired private User adminUser;
  @Autowired private StringResponseAssertion stringResponseAssertion;

  @Test
  @DisplayName("Update booking with all valid fields - basic auth")
  void updateBookingUsingBasicAuthTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    Booking bookingUpdate = bookingFactory.getWithAllValidFields();
    Response response = bookerClient.updateBooking(bookingId, bookingUpdate);
    bookingAssertion.assertResponseIsEqualTo(response, bookingUpdate);

    Response fetchResponse = bookerClient.getBookingById(String.valueOf(bookingId));
    bookingAssertion.assertResponseIsEqualTo(fetchResponse, bookingUpdate);

    bookingDetailsPool.push(
        BookingDetails.builder().bookingId(bookingId).booking(bookingUpdate).build());
  }

  @Test
  @DisplayName("Update booking with all valid fields - token auth")
  void updateBookingUsingTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    Booking bookingUpdate = bookingFactory.getWithAllValidFields();

    String token = bookerClientSteps.createToken(adminUser).token();

    Response response = bookerClient.updateBooking(bookingId, bookingUpdate, token);
    bookingAssertion.assertResponseIsEqualTo(response, bookingUpdate);

    Response fetchResponse = bookerClient.getBookingById(String.valueOf(bookingId));
    bookingAssertion.assertResponseIsEqualTo(fetchResponse, bookingUpdate);

    bookingDetailsPool.push(
        BookingDetails.builder().bookingId(bookingId).booking(bookingUpdate).build());
  }

  @Test
  @DisplayName("Should return: forbidden when updating booking with invalid token")
  void shouldNotUpdateBookingWithInvalidTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    Booking bookingUpdate = bookingFactory.getWithAllValidFields();
    String invalidToken = "invalid_token";
    Response response = bookerClient.updateBooking(bookingId, bookingUpdate, invalidToken);

    stringResponseAssertion.assertResponseIsForbidden(response);

    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Should return: method not allowed when booking ID does not exist")
  void shouldNotUpdateBookingWhenBookingIdDoesNotExistTest() {
    long nonExistentBookingId = BookerRandomUtils.randomNumber(100_000, 200_000);

    Booking bookingUpdate = bookingFactory.getWithAllValidFields();

    Response response = bookerClient.updateBooking((int) nonExistentBookingId, bookingUpdate);

    stringResponseAssertion.assertResponseIsMethodNotAllowed(response);
  }
}
