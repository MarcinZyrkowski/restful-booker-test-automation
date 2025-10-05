package org.example.booking.fetch;

import io.restassured.response.Response;
import org.example.assertion.response.booking.BookingAssertion;
import org.example.context.SpringTestContext;
import org.example.factory.booking.CreateBookingRequestFactory;
import org.example.model.dto.common.Booking;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Fetch Booking by Id")
class FetchBookingTest extends SpringTestContext {

  @Test
  @DisplayName("Fetch booking by id")
  void fetchBookingTest() {
    Booking createRequest = CreateBookingRequestFactory.getWithAllValidFields();
    int bookingId = bookerClientSteps.createBooking(createRequest).bookingId();

    Response fetchResponse = bookerClient.getBookingById(bookingId);

    BookingAssertion.assertThat(fetchResponse)
        .statusIsOk()
        .body()
        .isEqualTo(createRequest);
  }

}
