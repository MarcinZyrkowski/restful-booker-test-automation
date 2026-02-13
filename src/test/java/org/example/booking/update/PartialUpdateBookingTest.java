package org.example.booking.update;

import io.restassured.response.Response;
import org.example.SpringTestContext;
import org.example.model.dto.common.Booking;
import org.example.model.dto.response.booking.BookingDetails;
import org.example.model.enums.service.StringResponseBody;
import org.example.utils.BookerRandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Partial Update Booking")
class PartialUpdateBookingTest extends SpringTestContext {

  @Test
  @DisplayName("Partial update booking with all valid fields - basic auth")
  void partialUpdateBookingUsingBasicAuthTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrGet();
    int bookingId = bookingDetails.bookingId();

    Booking partialBookingUpdate = bookingFactory.getWithValidFieldsOrRandomlyNullFields();

    Response response = bookerClient.partialUpdateBooking(bookingId, partialBookingUpdate);
    Booking expectedBooking = bookingDetails.booking().mergeNonNullable(partialBookingUpdate);
    bookingAssertion.assertThat(response).statusIsOk().body().isEqualTo(expectedBooking);

    Response fetchResponse = bookerClient.getBookingById(bookingId);
    bookingAssertion.assertThat(fetchResponse).statusIsOk().body().isEqualTo(expectedBooking);

    bookingDetailsPool.push(
        BookingDetails.builder().bookingId(bookingId).booking(expectedBooking).build());
  }

  @Test
  @DisplayName("Partial update booking with all valid fields - token auth")
  void partialUpdateBookingUsingTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrGet();
    int bookingId = bookingDetails.bookingId();

    Booking partialBookingUpdate = bookingFactory.getWithValidFieldsOrRandomlyNullFields();
    String token = bookerClientSteps.createToken(adminUser).token();

    Response response = bookerClient.partialUpdateBooking(bookingId, partialBookingUpdate, token);
    Booking expectedBooking = bookingDetails.booking().mergeNonNullable(partialBookingUpdate);
    bookingAssertion.assertThat(response).statusIsOk().body().isEqualTo(expectedBooking);

    Response fetchResponse = bookerClient.getBookingById(bookingId);
    bookingAssertion.assertThat(fetchResponse).statusIsOk().body().isEqualTo(expectedBooking);

    bookingDetailsPool.push(
        BookingDetails.builder().bookingId(bookingId).booking(expectedBooking).build());
  }

  @Test
  @DisplayName("Should return: method not allowed when booking ID does not exist")
  void shouldNotPartialUpdateBookingWhenBookingIdDoesNotExistTest() {
    int nonExistentBookingId = BookerRandomUtils.RANDOM.randomInt(100000, 200000);

    Booking partialBookingUpdate = bookingFactory.getWithValidFieldsOrRandomlyNullFields();

    Response response =
        bookerClient.partialUpdateBooking(nonExistentBookingId, partialBookingUpdate);

    stringResponseAssertion
        .assertThat(response)
        .statusIsMethodNotAllowed()
        .body()
        .isEqualTo(StringResponseBody.METHOD_NOT_ALLOWED.getBody());
  }

  @Test
  @DisplayName("Should return: forbidden when updating booking with invalid token")
  void shouldNotPartialUpdateBookingWithInvalidTokenTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrGet();
    int bookingId = bookingDetails.bookingId();

    Booking partialBookingUpdate = bookingFactory.getWithValidFieldsOrRandomlyNullFields();
    String invalidToken = "invalid_token";

    Response response =
        bookerClient.partialUpdateBooking(bookingId, partialBookingUpdate, invalidToken);

    stringResponseAssertion
        .assertThat(response)
        .statusIsForbidden()
        .body()
        .isEqualTo(StringResponseBody.FORBIDDEN.getBody());

    bookingDetailsPool.push(bookingDetails);
  }
}
