package org.example.booking.fetch;

import io.restassured.response.Response;
import org.example.SpringTestContext;
import org.example.model.dto.response.booking.BookingDetails;
import org.example.model.enums.service.StringResponseBody;
import org.example.utils.BookerRandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Fetch Booking by Id")
class FetchBookingTest extends SpringTestContext {

  @Test
  @DisplayName("Fetch booking by id")
  void fetchBookingTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrGet();
    int bookingId = bookingDetails.bookingId();

    Response fetchResponse = bookerClient.getBookingById(bookingId);

    bookingAssertion
        .assertThat(fetchResponse)
        .statusIsOk()
        .body()
        .isEqualTo(bookingDetails.booking());

    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Fetch booking by id that doesn't exist")
  void fetchBookingByIdThatNotExistsTest() {
    int nonExistentBookingId = BookerRandomUtils.RANDOM.randomInt(100000, 200000);

    Response fetchResponse = bookerClient.getBookingById(nonExistentBookingId);

    stringResponseAssertion
        .assertThat(fetchResponse)
        .statusIsNotFound()
        .body()
        .isEqualTo(StringResponseBody.NOT_FOUND.getBody());
  }
}
