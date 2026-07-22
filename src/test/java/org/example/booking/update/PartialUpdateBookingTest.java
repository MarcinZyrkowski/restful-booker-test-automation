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
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    Booking partialBookingUpdate = bookingFactory.getWithValidOrNullFields();

    Booking response =
        partialUpdateBookingClient.partialUpdateBooking(bookingId, partialBookingUpdate);
    BookingAssert.assertThat(response)
        .isPartiallyUpdated(bookingDetails.booking(), partialBookingUpdate);

    Booking fetchResponse = fetchBookingClient.getBookingById(String.valueOf(bookingId));
    BookingAssert.assertThat(fetchResponse)
        .isPartiallyUpdated(bookingDetails.booking(), partialBookingUpdate);
  }

  @Test
  @DisplayName("Partial update booking with all valid fields - token auth")
  void partialUpdateBookingUsingTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    Booking partialBookingUpdate = bookingFactory.getWithValidOrNullFields();
    Token tokenResponse = tokenClient.createToken(adminUser);
    String token = tokenResponse.token();

    Booking response =
        partialUpdateBookingClient.partialUpdateBooking(bookingId, partialBookingUpdate, token);
    BookingAssert.assertThat(response)
        .isPartiallyUpdated(bookingDetails.booking(), partialBookingUpdate);

    Booking fetchResponse = fetchBookingClient.getBookingById(String.valueOf(bookingId));
    BookingAssert.assertThat(fetchResponse)
        .isPartiallyUpdated(bookingDetails.booking(), partialBookingUpdate);
  }

  @Test
  @DisplayName("Partial update booking with empty body - basic auth")
  void partialUpdateBookingWithEmptyBodyTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    Booking emptyBookingUpdate = Booking.builder().build();

    Booking response =
        partialUpdateBookingClient.partialUpdateBooking(bookingId, emptyBookingUpdate);
    BookingAssert.assertThat(response).isEqualToBooking(bookingDetails.booking());

    Booking fetchResponse = fetchBookingClient.getBookingById(String.valueOf(bookingId));
    BookingAssert.assertThat(fetchResponse).isEqualToBooking(bookingDetails.booking());

    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Should return: method not allowed when booking ID does not exist")
  void shouldNotPartialUpdateBookingWhenBookingIdDoesNotExistTest() {
    int nonExistentBookingId = BookerRandomUtils.randomInt(100_000, 200_000);

    Booking partialBookingUpdate = bookingFactory.getWithValidOrNullFields();

    String response =
        partialUpdateBookingClient.partialUpdateBookingExpectingError(
            nonExistentBookingId, partialBookingUpdate);

    StringResponseAssert.assertThat(response).isMethodNotAllowed();
  }

  @Test
  @DisplayName("Should return: forbidden when updating booking with invalid token")
  void shouldNotPartialUpdateBookingWithInvalidTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    Booking partialBookingUpdate = bookingFactory.getWithValidOrNullFields();
    String invalidToken = "invalid_token";

    String response =
        partialUpdateBookingClient.partialUpdateBookingExpectingError(
            bookingId, partialBookingUpdate, invalidToken);

    StringResponseAssert.assertThat(response).isForbidden();

    bookingDetailsPool.push(bookingDetails);
  }
}
