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
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    Booking bookingUpdate = bookingFactory.getWithAllValidFields();
    Booking response = updateBookingClient.updateBooking(bookingId, bookingUpdate);
    BookingAssert.assertThat(response).isEqualToBooking(bookingUpdate);

    Booking fetchedResponse = fetchBookingClient.getBookingById(String.valueOf(bookingId));
    BookingAssert.assertThat(fetchedResponse).isEqualToBooking(bookingUpdate);

    bookingDetailsPool.push(
        BookingDetails.builder().bookingId(bookingId).booking(bookingUpdate).build());
  }

  @Test
  @DisplayName("Update booking with all valid fields - token auth")
  void updateBookingUsingTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    Booking bookingUpdate = bookingFactory.getWithAllValidFields();

    Token tokenResponse = tokenClient.createToken(adminUser);
    String token = tokenResponse.token();

    Booking response = updateBookingClient.updateBooking(bookingId, bookingUpdate, token);
    BookingAssert.assertThat(response).isEqualToBooking(bookingUpdate);

    Booking fetchedResponse = fetchBookingClient.getBookingById(String.valueOf(bookingId));
    BookingAssert.assertThat(fetchedResponse).isEqualToBooking(bookingUpdate);

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
    String response =
        updateBookingClient.updateBookingExpectingError(bookingId, bookingUpdate, invalidToken);

    StringResponseAssert.assertThat(response).isForbidden();

    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Should return: method not allowed when booking ID does not exist")
  void shouldNotUpdateBookingWhenBookingIdDoesNotExistTest() {
    int nonExistentBookingId = BookerRandomUtils.randomInt(100_000, 200_000);

    Booking bookingUpdate = bookingFactory.getWithAllValidFields();

    String response =
        updateBookingClient.updateBookingExpectingError(nonExistentBookingId, bookingUpdate);

    StringResponseAssert.assertThat(response).isMethodNotAllowed();
  }
}
