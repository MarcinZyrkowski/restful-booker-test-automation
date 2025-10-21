package org.example.booking.update;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.example.assertion.response.booking.BookingAssertion;
import org.example.context.SpringTestContext;
import org.example.factory.auth.UserRequestFactory;
import org.example.factory.booking.BookingFactory;
import org.example.mapper.ObjMapper;
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
    BookingDetails bookingDetails =
        Allure.step(
            "Get or create booking for fetching",
            () -> {
              BookingDetails booking = bookingDetailsPool.popOrGet();
              Allure.attachment("booking details", ObjMapper.asJson(booking));
              return booking;
            });
    int bookingId = bookingDetails.bookingId();

    Booking bookingUpdate =
        Allure.step(
            "Prepare booking update request with all valid fields",
            () -> {
              Booking update = BookingFactory.getWithAllValidFields();
              Allure.attachment("booking", ObjMapper.asJson(update));
              return update;
            });

    Response response =
        Allure.step(
            "Send update booking request",
            () -> bookerClient.updateBooking(bookingId, bookingUpdate));

    Allure.step(
        "Verify booking is updated successfully",
        () -> {
          BookingAssertion.assertThat(response).statusIsOk().body().isEqualTo(bookingUpdate);
        });

    Response fetchResponse =
        Allure.step("Fetch booking by id", () -> bookerClient.getBookingById(bookingId));

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
    BookingDetails bookingDetails =
        Allure.step(
            "Get or create booking for fetching",
            () -> {
              BookingDetails booking = bookingDetailsPool.popOrGet();
              Allure.attachment("booking details", ObjMapper.asJson(booking));
              return booking;
            });
    int bookingId = bookingDetails.bookingId();

    Booking bookingUpdate =
        Allure.step(
            "Prepare booking update request with all valid fields",
            () -> {
              Booking update = BookingFactory.getWithAllValidFields();
              Allure.attachment("booking", ObjMapper.asJson(update));
              return update;
            });

    User user =
        Allure.step(
            "Prepare user for token authentication",
            () -> {
              User defaultUser = UserRequestFactory.defaultUser();
              Allure.attachment("user", ObjMapper.asJson(defaultUser));
              return defaultUser;
            });

    String token =
        Allure.step(
            "Create token for a user",
            () -> {
              String retrievedToken = bookerClientSteps.createToken(user).token();
              Allure.attachment("token", retrievedToken);
              return retrievedToken;
            });

    Response response =
        Allure.step(
            "Send update booking request",
            () -> bookerClient.updateBooking(bookingId, bookingUpdate, token));

    Allure.step(
        "Verify booking is updated successfully",
        () -> {
          BookingAssertion.assertThat(response).statusIsOk().body().isEqualTo(bookingUpdate);
        });

    Response fetchResponse =
        Allure.step("Fetch booking by id", () -> bookerClient.getBookingById(bookingId));

    Allure.step(
        "Assert fetch response",
        () -> {
          BookingAssertion.assertThat(fetchResponse).statusIsOk().body().isEqualTo(bookingUpdate);
        });

    bookingDetailsPool.push(
        BookingDetails.builder().bookingId(bookingId).booking(bookingUpdate).build());
  }
}
