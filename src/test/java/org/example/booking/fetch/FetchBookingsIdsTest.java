package org.example.booking.fetch;

import io.qameta.allure.Issue;
import io.restassured.response.Response;
import org.example.SpringTestContext;
import org.example.model.dto.common.Booking;
import org.example.model.dto.response.booking.BookingDetails;
import org.example.tracking.Bugs;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Fetch Booking Ids")
class FetchBookingsIdsTest extends SpringTestContext {

  @Test
  @DisplayName("Fetch all booking ids")
  void fetchAllBookingIdsTest() {
    Response response = bookerClient.getBookingIds(null, null, null, null);

    bookingIdAssertion.assertThat(response).statusIsOk().body().hasBookingIds();
  }

  @Test
  @DisplayName("Fetch booking ids with filter by first name")
  void fetchBookingIdsWithFilterByFirstNameTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrGet();
    Booking booking = bookingDetails.booking();

    Response response = bookerClient.getBookingIds(booking.firstName(), null, null, null);

    bookingIdAssertion
        .assertThat(response)
        .statusIsOk()
        .body()
        .hasBookingId(bookingDetails.bookingId());

    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Fetch booking ids with filter by last name")
  void fetchBookingIdsWithFilterByLastNameTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrGet();
    Booking booking = bookingDetails.booking();

    Response response = bookerClient.getBookingIds(null, booking.lastName(), null, null);

    bookingIdAssertion
        .assertThat(response)
        .statusIsOk()
        .body()
        .hasBookingId(bookingDetails.bookingId());

    bookingDetailsPool.push(bookingDetails);
  }

  // TODO add dates mapper, verify that strick equals work!
  // TODO add case when check in filter (random) < booking check in
  @Issue(value = Bugs.CHECK_IN_BUG)
  @Disabled
  @Test
  @DisplayName("Fetch booking ids with filter by check in date")
  void fetchBookingIdsWithFilterByCheckInDateTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrGet();
    Booking booking = bookingDetails.booking();

    Response response =
        bookerClient.getBookingIds(null, null, booking.bookingDates().checkIn(), null);

    bookingIdAssertion
        .assertThat(response)
        .statusIsOk()
        .body()
        .hasBookingId(bookingDetails.bookingId());

    bookingDetailsPool.push(bookingDetails);
  }

  // TODO add case when check out filter (random) < booking check out


  @Test
  @DisplayName("Fetch booking ids with filter by checkout date")
  void fetchBookingIdsWithFilterByCheckOutDateTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrGet();
    Booking booking = bookingDetails.booking();

    Response response =
        bookerClient.getBookingIds(null, null, null, booking.bookingDates().checkOut());

    bookingIdAssertion
        .assertThat(response)
        .statusIsOk()
        .body()
        .hasBookingId(bookingDetails.bookingId());

    bookingDetailsPool.push(bookingDetails);
  }
}
