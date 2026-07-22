package org.example.booking.delete;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.assertion.common.StringResponseAssert;
import org.example.client.booking.DeleteBookingClient;
import org.example.client.booking.FetchBookingClient;
import org.example.client.token.TokenClient;
import org.example.config.SpringConfig;
import org.example.model.service.dto.request.auth.User;
import org.example.model.service.dto.response.auth.Token;
import org.example.model.service.dto.response.booking.BookingDetails;
import org.example.pool.BookingDetailsPool;
import org.example.tags.Regression;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Severity(SeverityLevel.CRITICAL)
@Epic("Booking API")
@Feature("Booking Management")
@Story("Delete Booking")
@Regression
@SpringBootTest(classes = SpringConfig.class)
@DisplayName("Delete Booking")
class DeleteBookingTest {

  @Autowired private BookingDetailsPool bookingDetailsPool;
  @Autowired private DeleteBookingClient deleteBookingClient;
  @Autowired private FetchBookingClient fetchBookingClient;
  @Autowired private TokenClient tokenClient;
  @Autowired private User adminUser;

  @Test
  @DisplayName("Delete booking using basic auth")
  void deleteBookingUsingBasicAuthTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    String deleteResponse = deleteBookingClient.deleteBooking(bookingId);
    StringResponseAssert.assertThat(deleteResponse).isCreated();

    String fetchResponse =
        fetchBookingClient.getBookingByIdExpectingError(String.valueOf(bookingId));
    StringResponseAssert.assertThat(fetchResponse).isNotFound();
  }

  @Test
  @DisplayName("Delete booking using token")
  void deleteBookingUsingTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    Token tokenResponse = tokenClient.createToken(adminUser);
    String token = tokenResponse.token();

    String deleteResponse = deleteBookingClient.deleteBooking(bookingId, token);
    StringResponseAssert.assertThat(deleteResponse).isCreated();

    String fetchResponse =
        fetchBookingClient.getBookingByIdExpectingError(String.valueOf(bookingId));
    StringResponseAssert.assertThat(fetchResponse).isNotFound();
  }

  @Test
  @DisplayName("Delete booking using invalid token")
  void deleteBookingUsingInvalidTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    String invalidToken = "invalid-token";
    String response = deleteBookingClient.deleteBookingExpectingError(bookingId, invalidToken);

    StringResponseAssert.assertThat(response).isForbidden();
  }
}
