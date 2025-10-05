package org.example.booking.create;

import io.restassured.response.Response;
import org.example.assertion.response.StringResponseAssertion;
import org.example.assertion.response.booking.CreatedBookingAssertion;
import org.example.context.SpringTestContext;
import org.example.factory.booking.CreateBookingRequestFactory;
import org.example.model.dto.common.Booking;
import org.example.model.enums.service.StringResponseBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Create Booking")
class CreateBookingTest extends SpringTestContext {

  @Test
  @DisplayName("Create booking with all valid fields")
  void createBookingTest() {
    Booking request = CreateBookingRequestFactory.getWithAllValidFields();

    Response response = bookerClient.createBooking(request);

    CreatedBookingAssertion.assertThat(response)
        .statusIsOk()
        .body()
        .isCreatedFrom(request);
  }

  @DisplayName("Should not create booking with missing required field")
  @ParameterizedTest(name = "{1}")
  @MethodSource("org.example.dataprovider.BookingDataProvider#missingFieldBookingRequest")
  void shouldNotCreateBookingTest(Booking request, String string) {
    Response response = bookerClient.createBooking(request);

    StringResponseAssertion.assertThat(response)
        .statusIsInternalServerError()
        .body()
        .isEqualTo(StringResponseBody.INTERNAL_SERVER_ERROR.getBody());
  }

}
