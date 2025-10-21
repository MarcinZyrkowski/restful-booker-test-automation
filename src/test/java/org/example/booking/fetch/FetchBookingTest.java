package org.example.booking.fetch;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.example.assertion.response.booking.BookingAssertion;
import org.example.context.SpringTestContext;
import org.example.mapper.ObjMapper;
import org.example.model.dto.response.booking.BookingDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Fetch Booking by Id")
class FetchBookingTest extends SpringTestContext {

  @Test
  @DisplayName("Fetch booking by id")
  void fetchBookingTest() {
    BookingDetails bookingDetails =
        Allure.step(
            "Get or create booking for fetching",
            () -> {
              BookingDetails booking = bookingDetailsPool.popOrGet();
              Allure.attachment("booking details", ObjMapper.asJson(booking));
              return booking;
            });
    int bookingId = bookingDetails.bookingId();

    Response fetchResponse =
        Allure.step("Fetch booking by id", () -> bookerClient.getBookingById(bookingId));

    Allure.step(
        "Assert fetch response",
        () -> {
          BookingAssertion.assertThat(fetchResponse)
              .statusIsOk()
              .body()
              .isEqualTo(bookingDetails.booking());
        });

    bookingDetailsPool.push(bookingDetails);
  }
}
