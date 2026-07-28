package org.example.booking.delete;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
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
import org.example.tracking.Bugs;
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
  @Issue(value = Bugs.DELETE_RETURNS_201_CREATED_BUG)
  @DisplayName("Delete booking using basic auth")
  void deleteBookingUsingBasicAuthTest() {
    // Retrieve an existing booking from the pool or create a new one
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    // Send request to delete the booking using basic auth
    String deleteResponse = deleteBookingClient.deleteBooking(bookingId);
    // Verify a created status code is returned for deletion
    StringResponseAssert.assertThat(deleteResponse).isCreated();

    // Attempt to fetch the deleted booking and expect an error
    String fetchResponse =
        fetchBookingClient.getBookingByIdExpectingError(String.valueOf(bookingId));
    // Verify the booking is no longer found
    StringResponseAssert.assertThat(fetchResponse).isNotFound();
  }

  @Test
  @Issue(value = Bugs.DELETE_RETURNS_201_CREATED_BUG)
  @DisplayName("Delete booking using token")
  void deleteBookingUsingTokenTest() {
    // Retrieve an existing booking from the pool or create a new one
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    // Generate a valid authentication token
    Token tokenResponse = tokenClient.createToken(adminUser);
    String token = tokenResponse.token();

    // Send request to delete the booking using the generated token
    String deleteResponse = deleteBookingClient.deleteBooking(bookingId, token);
    // Verify a created status code is returned for deletion
    StringResponseAssert.assertThat(deleteResponse).isCreated();

    // Attempt to fetch the deleted booking and expect an error
    String fetchResponse =
        fetchBookingClient.getBookingByIdExpectingError(String.valueOf(bookingId));
    // Verify the booking is no longer found
    StringResponseAssert.assertThat(fetchResponse).isNotFound();
  }

  @Test
  @DisplayName("Delete booking using invalid token")
  void deleteBookingUsingInvalidTokenTest() {
    // Retrieve an existing booking from the pool or create a new one
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    // Attempt to delete the booking using an invalid token and expect an error
    String invalidToken = "invalid-token";
    String response = deleteBookingClient.deleteBookingExpectingError(bookingId, invalidToken);

    // Verify a forbidden status code is returned
    StringResponseAssert.assertThat(response).isForbidden();
  }
}
