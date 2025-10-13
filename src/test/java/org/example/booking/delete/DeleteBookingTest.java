package org.example.booking.delete;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.example.assertion.response.StringResponseAssertion;
import org.example.context.SpringTestContext;
import org.example.factory.auth.UserRequestFactory;
import org.example.mapper.ObjMapper;
import org.example.model.dto.request.auth.User;
import org.example.model.dto.response.booking.CreatedBooking;
import org.example.model.enums.service.StringResponseBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Delete Booking")
class DeleteBookingTest extends SpringTestContext {

  @Test
  @DisplayName("Delete booking using basic auth")
  void deleteBookingUsingBasicAuthTest() {
    CreatedBooking createdBooking = Allure.step("Get or create booking for fetching", () -> {
      CreatedBooking booking = createdBookingPool.popOrGet();
      Allure.step("createdBooking: " + ObjMapper.asJson(booking));
      return booking;
    });
    int bookingId = createdBooking.bookingId();

    Response deleteResponse = Allure.step("Delete booking with basic auth",
        () -> bookerClient.deleteBooking(bookingId));

    Allure.step("Verify booking is deleted successfully", () -> {
      StringResponseAssertion.assertThat(deleteResponse)
          .statusIsCreated()
          .body()
          .isEqualTo(StringResponseBody.CREATED.getBody());
    });

    Allure.step("Verify booking is deleted (with get)", () ->
        bookerClientSteps.fetchBookingAssertNotFound(bookingId));
  }

  @Test
  @DisplayName("Delete booking using token")
  void deleteBookingUsingTokenTest() {
    CreatedBooking createdBooking = Allure.step("Get or create booking for fetching", () -> {
      CreatedBooking booking = createdBookingPool.popOrGet();
      Allure.step("createdBooking: " + ObjMapper.asJson(booking));
      return booking;
    });
    int bookingId = createdBooking.bookingId();

    User user = Allure.step("Prepare user for token authentication", () -> {
      User defaultUser = UserRequestFactory.defaultUser();
      Allure.step("user: " + ObjMapper.asJson(defaultUser));
      return defaultUser;
    });

    String token = Allure.step("Create token for a user", () -> {
      String retrievedToken = bookerClientSteps.createToken(user).token();
      Allure.step("token: " + retrievedToken);
      return retrievedToken;
    });

    Response deleteResponse = Allure.step("Delete booking using token",
        () -> bookerClient.deleteBooking(bookingId, token));

    Allure.step("Verify booking is deleted successfully", () -> {
      StringResponseAssertion.assertThat(deleteResponse)
          .statusIsCreated()
          .body()
          .isEqualTo(StringResponseBody.CREATED.getBody());
    });

    Allure.step("Verify booking is deleted (with get)", () ->
        bookerClientSteps.fetchBookingAssertNotFound(bookingId));
  }

}
