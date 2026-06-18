package org.example.booking.delete;

import io.restassured.response.Response;
import org.example.assertion.common.StringResponseAssertion;
import org.example.client.BookerClient;
import org.example.config.SpringConfig;
import org.example.model.service.dto.request.auth.User;
import org.example.model.service.dto.response.booking.BookingDetails;
import org.example.pool.BookingDetailsPool;
import org.example.steps.BookerClientSteps;
import org.example.tags.Regression;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Regression
@SpringBootTest(classes = SpringConfig.class)
@DisplayName("Delete Booking")
class DeleteBookingTest {

  @Autowired private BookingDetailsPool bookingDetailsPool;
  @Autowired private BookerClient bookerClient;
  @Autowired private StringResponseAssertion stringResponseAssertion;
  @Autowired private BookerClientSteps bookerClientSteps;
  @Autowired private User adminUser;

  @Test
  @DisplayName("Delete booking using basic auth")
  void deleteBookingUsingBasicAuthTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    Response deleteResponse = bookerClient.deleteBooking(bookingId);

    stringResponseAssertion.assertResponseIsCreated(deleteResponse);
    bookerClientSteps.fetchBookingAssertNotFound(bookingId);
  }

  @Test
  @DisplayName("Delete booking using token")
  void deleteBookingUsingTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    String token = bookerClientSteps.createToken(adminUser).token();

    Response deleteResponse = bookerClient.deleteBooking(bookingId, token);

    stringResponseAssertion.assertResponseIsCreated(deleteResponse);
    bookerClientSteps.fetchBookingAssertNotFound(bookingId);
  }

  @Test
  @DisplayName("Delete booking using invalid token")
  void deleteBookingUsingInvalidTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    int bookingId = bookingDetails.bookingId();

    String invalidToken = "invalid-token";
    Response deleteResponse = bookerClient.deleteBooking(bookingId, invalidToken);

    stringResponseAssertion.assertResponseIsForbidden(deleteResponse);
  }
}
