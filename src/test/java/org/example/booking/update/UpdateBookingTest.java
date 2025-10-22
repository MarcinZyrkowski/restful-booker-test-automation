package org.example.booking.update;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.example.SpringTestContext;
import org.example.assertion.response.booking.BookingAssertion;
import org.example.factory.auth.UserRequestFactory;
import org.example.factory.booking.BookingFactory;
import org.example.model.dto.common.Booking;
import org.example.model.dto.request.auth.User;
import org.example.model.dto.response.booking.BookingDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Update Booking")
class UpdateBookingTest extends SpringTestContext {

  @Test
  @DisplayName("Update booking with all valid fields - basic auth")
  void updateBookingUsingBasicAuthTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrGet();
    int bookingId = bookingDetails.bookingId();

    Booking bookingUpdate = BookingFactory.getWithAllValidFields();

    Response response = bookerClient.updateBooking(bookingId, bookingUpdate);

    Allure.step(
        "Verify booking is updated successfully",
        () -> {
          BookingAssertion.assertThat(response).statusIsOk().body().isEqualTo(bookingUpdate);
        });

    Response fetchResponse = bookerClient.getBookingById(bookingId);

    Allure.step(
        "Assert fetch response",
        () -> {
          BookingAssertion.assertThat(fetchResponse).statusIsOk().body().isEqualTo(bookingUpdate);
        });

    bookingDetailsPool.push(
        BookingDetails.builder().bookingId(bookingId).booking(bookingUpdate).build());
  }

  @Test
  @DisplayName("Update booking with all valid fields - token auth")
  void updateBookingUsingTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrGet();
    int bookingId = bookingDetails.bookingId();

    Booking bookingUpdate = BookingFactory.getWithAllValidFields();

    User user = UserRequestFactory.defaultUser();

    String token = bookerClientSteps.createToken(user).token();

    Response response = bookerClient.updateBooking(bookingId, bookingUpdate, token);

    Allure.step(
        "Verify booking is updated successfully",
        () -> {
          BookingAssertion.assertThat(response).statusIsOk().body().isEqualTo(bookingUpdate);
        });

    Response fetchResponse = bookerClient.getBookingById(bookingId);

    Allure.step(
        "Assert fetch response",
        () -> {
          BookingAssertion.assertThat(fetchResponse).statusIsOk().body().isEqualTo(bookingUpdate);
        });

    bookingDetailsPool.push(
        BookingDetails.builder().bookingId(bookingId).booking(bookingUpdate).build());
  }
}
