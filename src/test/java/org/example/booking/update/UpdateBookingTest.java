package org.example.booking.update;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.example.assertion.response.booking.BookingAssertion;
import org.example.context.SpringTestContext;
import org.example.factory.booking.BookingFactory;
import org.example.mapper.ObjMapper;
import org.example.model.dto.common.Booking;
import org.example.model.dto.response.booking.CreatedBooking;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Update Booking")
class UpdateBookingTest extends SpringTestContext {

  @Test
  @DisplayName("Update booking with all valid fields - basic auth")
  void updateBookingWithBasicAuthTest() {
    CreatedBooking createdBooking = Allure.step("Get or create booking for fetching", () -> {
      CreatedBooking booking = createdBookingPool.popOrGet();
      Allure.step("createdBooking: " + ObjMapper.asJson(booking));
      return booking;
    });
    int bookingId = createdBooking.bookingId();

    Booking bookingUpdate = Allure.step("Prepare booking update request with all valid fields",
        () -> {
          Booking update = BookingFactory.getWithAllValidFields();
          Allure.step("bookingUpdate: " + ObjMapper.asJson(update));
          return update;
        }
    );

    Response response = Allure.step("Send update booking request",
        () -> bookerClient.updateBooking(bookingId, bookingUpdate)
    );

    Allure.step("Verify booking is updated successfully", () -> {
      BookingAssertion.assertThat(response)
          .statusIsOk()
          .body()
          .isEqualTo(bookingUpdate);
    });

    Response fetchResponse = Allure.step("Fetch booking by id", () ->
        bookerClient.getBookingById(bookingId));

    Allure.step("Assert fetch response", () -> {
      BookingAssertion.assertThat(fetchResponse)
          .statusIsOk()
          .body()
          .isEqualTo(bookingUpdate);
    });

    createdBookingPool.push(CreatedBooking.builder()
        .bookingId(bookingId)
        .booking(bookingUpdate)
        .build());
  }

}
