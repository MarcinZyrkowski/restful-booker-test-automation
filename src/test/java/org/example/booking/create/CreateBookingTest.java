package org.example.booking.create;

import io.restassured.response.Response;
import org.example.assertion.response.booking.CreateBookingResponseAssertion;
import org.example.context.SpringTestContext;
import org.example.model.dto.request.booking.BookingRequestResponse;
import org.junit.jupiter.api.Test;

class CreateBookingTest extends SpringTestContext {

  @Test
  void createBookingTest() {
    BookingRequestResponse request = createBookingRequestFactory.getWithOnlyRequiredFields();

    Response response = bookerClient.createBooking(request);

    CreateBookingResponseAssertion.assertThat(response)
        .statusIsOk()
        .body()
        .isCreatedFrom(request);
  }

}
