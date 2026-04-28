package org.example.booking.create;

import io.restassured.response.Response;
import java.util.stream.Stream;
import org.example.SpringTestContext;
import org.example.model.dto.common.Booking;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Create Booking")
class CreateBookingTest extends SpringTestContext {

  @Test
  @DisplayName("Create booking with all valid fields")
  void createBookingTest() {
    Booking requestBody = bookingFactory.getWithAllValidFields();

    Response response = bookerClient.createBooking(requestBody);

    bookingDetailsAssertion.assertResponseIsCreatedFrom(response, requestBody);

    bookingDetailsPool.push(response);
  }

  @DisplayName("Should not create booking with missing required field")
  @ParameterizedTest(name = "{1}")
  @MethodSource("providerMissingFieldBookings")
  void shouldNotCreateBookingTest(Booking request, String description) {
    Response response = bookerClient.createBooking(request);

    stringResponseAssertion.assertResponseIsInternalServerError(response);
  }

  Stream<Arguments> providerMissingFieldBookings() {
    return bookingDataProvider.missingFieldBookings();
  }
}
