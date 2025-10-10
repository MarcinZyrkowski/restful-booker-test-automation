package org.example.booking.fetch;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.example.assertion.response.booking.BookingAssertion;
import org.example.context.SpringTestContext;
import org.example.model.dto.response.booking.CreatedBooking;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Fetch Booking by Id")
class FetchBookingTest extends SpringTestContext {

  @Test
  @DisplayName("Fetch booking by id")
  void fetchBookingTest() {
    CreatedBooking createdBooking = Allure.step("Get or create booking for fetching", () ->
        createdBookingPool.popOrGet());

    int bookingId = Allure.step("Retrieve bookingId",
        createdBooking::bookingId);

    Response fetchResponse = Allure.step("Fetch booking by id", () ->
        bookerClient.getBookingById(bookingId));

    Allure.step("Assert fetch response", () -> {
      BookingAssertion.assertThat(fetchResponse)
          .statusIsOk()
          .body()
          .isEqualTo(createdBooking.booking());
    });

    createdBookingPool.push(createdBooking);
  }

}
