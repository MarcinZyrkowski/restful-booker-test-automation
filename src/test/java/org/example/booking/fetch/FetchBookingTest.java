package org.example.booking.fetch;

import io.restassured.response.Response;
import org.example.SpringTestContext;
import org.example.model.dto.response.booking.BookingDetails;
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
}
