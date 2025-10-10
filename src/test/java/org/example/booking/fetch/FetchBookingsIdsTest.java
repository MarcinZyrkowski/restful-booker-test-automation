package org.example.booking.fetch;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.example.assertion.response.booking.BookingIdAssertion;
import org.example.context.SpringTestContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Fetch Booking Ids")
class FetchBookingsIdsTest extends SpringTestContext {

  @Test
  @DisplayName("Fetch all booking ids")
  void fetchAllBookingIdsTest() {
    Response response = Allure.step("Fetch all booking ids", () ->
        bookerClient.getBookingIds(null, null, null, null));

    Allure.step("Verify response contains booking ids", () -> {
      BookingIdAssertion.assertThat(response)
          .statusIsOk()
          .body()
          .hasBookingIds();
    });
  }
}
