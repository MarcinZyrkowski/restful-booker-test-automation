package org.example.booking.update;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.example.assertion.response.booking.BookingAssertion;
import org.example.context.SpringTestContext;
import org.example.factory.booking.BookingFactory;
import org.example.mapper.ObjMapper;
import org.example.model.dto.common.Booking;
import org.example.model.dto.response.booking.BookingDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Partial Update Booking")
class PartialUpdateBookingTest extends SpringTestContext {

  @Test
  @DisplayName("Partial update booking with all valid fields - basic auth")
  void partialUpdateBookingUsingBasicAuthTest() {
    BookingDetails bookingDetails = Allure.step("Get or create booking for fetching", () -> {
      BookingDetails booking = bookingDetailsPool.popOrGet();
      Allure.attachment("booking details", ObjMapper.asJson(booking));
      return booking;
    });
    int bookingId = bookingDetails.bookingId();

    Booking partialBookingUpdate = Allure.step(
        "Prepare booking update request with all valid fields",
        () -> {
          Booking update = BookingFactory.getWithValidFieldsOrRandomlyNullFields();
          Allure.attachment("booking", ObjMapper.asJson(update));
          return update;
        }
    );

    Response response = Allure.step("Send partial update booking request",
        () -> bookerClient.partialUpdateBooking(bookingId, partialBookingUpdate)
    );

    Booking expectedBooking = Allure.step("Prepare expected booking details after update", () -> {
          Booking booking = bookingDetails.booking()
              .mergeNonNullable(partialBookingUpdate);
          Allure.attachment("expected booking", ObjMapper.asJson(booking));
          return booking;
        }
    );

    Allure.step("Verify booking is partially updated successfully", () -> {
      BookingAssertion.assertThat(response)
          .statusIsOk()
          .body()
          .isEqualTo(expectedBooking);
    });

    Response fetchResponse = Allure.step("Fetch booking by id", () ->
        bookerClient.getBookingById(bookingId));

    Allure.step("Assert fetch response", () -> {
      BookingAssertion.assertThat(fetchResponse)
          .statusIsOk()
          .body()
          .isEqualTo(expectedBooking);
    });

    bookingDetailsPool.push(BookingDetails.builder()
        .bookingId(bookingId)
        .booking(expectedBooking)
        .build());
  }

}
