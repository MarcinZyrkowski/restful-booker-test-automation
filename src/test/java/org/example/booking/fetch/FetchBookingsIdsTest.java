package org.example.booking.fetch;

import io.restassured.response.Response;
import org.example.assertion.response.BookingIdAssertion;
import org.example.context.SpringTestContext;
import org.junit.jupiter.api.Test;

class FetchBookingsIdsTest extends SpringTestContext {

  @Test
  void fetchAllBookingIdsTest() {
    Response response = bookerClient.getBookingIds(null, null, null, null);

    BookingIdAssertion.assertThat(response)
        .statusIsOk()
        .body()
        .hasBookingIds();
  }

}
