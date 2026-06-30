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
@DisplayName("Partial Update Booking")
class PartialUpdateBookingTest {

  @Autowired private BookingDetailsPool bookingDetailsPool;
  @Autowired private BookingFactory bookingFactory;
  @Autowired private BookerClient bookerClient;
  @Autowired private BookingAssertion bookingAssertion;
  @Autowired private BookerClientSteps bookerClientSteps;
  @Autowired private User adminUser;
  @Autowired private StringResponseAssertion stringResponseAssertion;

  @Test
  @DisplayName("Partial update booking with all valid fields - basic auth")
  void partialUpdateBookingUsingBasicAuthTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    Booking partialBookingUpdate = bookingFactory.getWithValidOrNullFields();

    Response partialUpdateResponse =
        bookerClient.partialUpdateBooking(bookingId, partialBookingUpdate);
    bookingAssertion.assertBookingIsPartiallyUpdated(
        partialUpdateResponse, bookingDetails.booking(), partialBookingUpdate);

    Response fetchResponse = bookerClient.getBookingById(String.valueOf(bookingId));
    bookingAssertion.assertBookingIsPartiallyUpdated(
        fetchResponse, bookingDetails.booking(), partialBookingUpdate);
  }

  @Test
  @DisplayName("Partial update booking with all valid fields - token auth")
  void partialUpdateBookingUsingTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    Booking partialBookingUpdate = bookingFactory.getWithValidOrNullFields();
    String token = bookerClientSteps.createToken(adminUser).token();

    Response partialUpdateResponse =
        bookerClient.partialUpdateBooking(bookingId, partialBookingUpdate, token);
    bookingAssertion.assertBookingIsPartiallyUpdated(
        partialUpdateResponse, bookingDetails.booking(), partialBookingUpdate);

    Response fetchResponse = bookerClient.getBookingById(String.valueOf(bookingId));
    bookingAssertion.assertBookingIsPartiallyUpdated(
        fetchResponse, bookingDetails.booking(), partialBookingUpdate);
  }

  @Test
  @DisplayName("Partial update booking with empty body - basic auth")
  void partialUpdateBookingWithEmptyBodyTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    Booking emptyBookingUpdate = Booking.builder().build();

    Response partialUpdateResponse =
        bookerClient.partialUpdateBooking(bookingId, emptyBookingUpdate);
    bookingAssertion.assertResponseIsEqualTo(partialUpdateResponse, bookingDetails.booking());

    Response fetchResponse = bookerClient.getBookingById(String.valueOf(bookingId));
    bookingAssertion.assertResponseIsEqualTo(fetchResponse, bookingDetails.booking());

    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Should return: method not allowed when booking ID does not exist")
  void shouldNotPartialUpdateBookingWhenBookingIdDoesNotExistTest() {
    long nonExistentBookingId = BookerRandomUtils.randomNumber(100_000, 200_000);

    Booking partialBookingUpdate = bookingFactory.getWithValidOrNullFields();

    Response response =
        bookerClient.partialUpdateBooking((int) nonExistentBookingId, partialBookingUpdate);

    stringResponseAssertion.assertResponseIsMethodNotAllowed(response);
  }

  @Test
  @DisplayName("Should return: forbidden when updating booking with invalid token")
  void shouldNotPartialUpdateBookingWithInvalidTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    Booking partialBookingUpdate = bookingFactory.getWithValidOrNullFields();
    String invalidToken = "invalid_token";

    Response response =
        bookerClient.partialUpdateBooking(bookingId, partialBookingUpdate, invalidToken);

    stringResponseAssertion.assertResponseIsForbidden(response);

    bookingDetailsPool.push(bookingDetails);
  }
}
