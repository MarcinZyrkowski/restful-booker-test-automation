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

@DisplayName("Partial Update Booking")
class PartialUpdateBookingTest extends SpringTestContext {

  @Test
  @DisplayName("Partial update booking with all valid fields - basic auth")
  void partialUpdateBookingUsingBasicAuthTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrGet();
    int bookingId = bookingDetails.bookingId();

    Booking partialBookingUpdate = BookingFactory.getWithValidFieldsOrRandomlyNullFields();

    Response response = bookerClient.partialUpdateBooking(bookingId, partialBookingUpdate);

    Booking expectedBooking = bookingDetails.booking().mergeNonNullable(partialBookingUpdate);

    Allure.step(
        "Verify booking is partially updated successfully",
        () -> {
          BookingAssertion.assertThat(response).statusIsOk().body().isEqualTo(expectedBooking);
        });

    Response fetchResponse = bookerClient.getBookingById(bookingId);

    Allure.step(
        "Assert fetch response",
        () -> {
          BookingAssertion.assertThat(fetchResponse).statusIsOk().body().isEqualTo(expectedBooking);
        });

    bookingDetailsPool.push(
        BookingDetails.builder().bookingId(bookingId).booking(expectedBooking).build());
  }

  @Test
  @DisplayName("Partial update booking with all valid fields - token auth")
  void partialUpdateBookingUsingTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrGet();
    int bookingId = bookingDetails.bookingId();

    Booking partialBookingUpdate = BookingFactory.getWithValidFieldsOrRandomlyNullFields();

    User user = UserRequestFactory.defaultUser();

    String token = bookerClientSteps.createToken(user).token();

    Response response = bookerClient.partialUpdateBooking(bookingId, partialBookingUpdate, token);

    Booking expectedBooking = bookingDetails.booking().mergeNonNullable(partialBookingUpdate);

    Allure.step(
        "Verify booking is partially updated successfully",
        () -> {
          BookingAssertion.assertThat(response).statusIsOk().body().isEqualTo(expectedBooking);
        });

    Response fetchResponse = bookerClient.getBookingById(bookingId);

    Allure.step(
        "Assert fetch response",
        () -> {
          BookingAssertion.assertThat(fetchResponse).statusIsOk().body().isEqualTo(expectedBooking);
        });

    bookingDetailsPool.push(
        BookingDetails.builder().bookingId(bookingId).booking(expectedBooking).build());
  }
}
