package org.example.booking.create;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.example.assertion.response.StringResponseAssertion;
import org.example.assertion.response.booking.CreatedBookingAssertion;
import org.example.context.SpringTestContext;
import org.example.factory.booking.BookingFactory;
import org.example.mapper.ObjMapper;
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
    Booking request = Allure.step("Prepare booking request with all valid fields", () -> {
          Booking body = BookingFactory.getWithAllValidFields();
          Allure.step("request: " + ObjMapper.asJson(body));
          return body;
        }
    );

    Response response = Allure.step("Send create booking request",
        () -> bookerClient.createBooking(request)
    );

    Allure.step("Verify booking is created successfully", () -> {
      CreatedBookingAssertion.assertThat(response)
          .statusIsOk()
          .body()
          .isCreatedFrom(request);
    });

    createdBookingPool.push(response);
  }

  @DisplayName("Should not create booking with missing required field")
  @ParameterizedTest(name = "{1}")
  @MethodSource("org.example.dataprovider.BookingDataProvider#missingFieldBookingRequest")
  void shouldNotCreateBookingTest(Booking request, String string) {
    Response response = Allure.step("Send create booking request with missing field",
        () -> bookerClient.createBooking(request));

    Allure.step("Verify response is internal server error", () -> {
      StringResponseAssertion.assertThat(response)
          .statusIsInternalServerError()
          .body()
          .isEqualTo(StringResponseBody.INTERNAL_SERVER_ERROR.getBody());
    });
  }

}
