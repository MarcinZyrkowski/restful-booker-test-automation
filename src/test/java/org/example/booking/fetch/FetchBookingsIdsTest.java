package org.example.booking.fetch;

import io.qameta.allure.Issue;
import io.restassured.response.Response;
import java.time.LocalDate;
import org.example.SpringTestContext;
import org.example.generator.DateTimesGenerator;
import org.example.model.dto.common.Booking;
import org.example.model.dto.response.booking.BookingDetails;
import org.example.tracking.Bugs;
import org.example.utils.BookerRandomUtils;
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

  @Issue(value = Bugs.CHECK_IN_BUG)
  @Disabled(value = "Skipped because of bug: " + Bugs.CHECK_IN_BUG)
  @Test
  @DisplayName("Fetch booking ids with filter by check in date (equal to booking check in date)")
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

  @Test
  @DisplayName(
      "Fetch booking ids with filter by check in date (earlier than booking check in date)")
  void fetchBookingIdsWithFilterByCheckInDateEarlierThanTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrGet();
    Booking booking = bookingDetails.booking();

    LocalDate bookingCheckIn = dateMapper.mapStringToLocalDate(booking.bookingDates().checkIn());
    LocalDate filterCheckIn = DateTimesGenerator.getRandomDateBefore(bookingCheckIn, 50);

    Response response = bookerClient.getBookingIds(null, null, filterCheckIn.toString(), null);

    bookingIdAssertion
        .assertThat(response)
        .statusIsOk()
        .body()
        .hasBookingId(bookingDetails.bookingId());

    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Fetch booking ids with filter by checkout date (equal to booking checkout date)")
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

  @Issue(value = Bugs.CHECK_OUT_BUG)
  @Disabled(value = "Skipped because of bug: " + Bugs.CHECK_OUT_BUG)
  @Test
  @DisplayName(
      "Fetch booking ids with filter by checkout date (earlier than booking checkout date)")
  void fetchBookingIdsWithFilterByCheckOutDateLessThanTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrGet();
    Booking booking = bookingDetails.booking();

    LocalDate bookingCheckOut = LocalDate.parse(booking.bookingDates().checkOut());
    LocalDate filterCheckOut = DateTimesGenerator.getRandomDateBefore(bookingCheckOut, 50);

    Response response = bookerClient.getBookingIds(null, null, null, filterCheckOut.toString());

    bookingIdAssertion
        .assertThat(response)
        .statusIsOk()
        .body()
        .hasBookingId(bookingDetails.bookingId());

    bookingDetailsPool.push(bookingDetails);
  }

  @Issue(value = Bugs.CHECK_IN_BUG)
  @Disabled(value = "Skipped because of bug: " + Bugs.CHECK_IN_BUG)
  @Test
  @DisplayName("Fetch booking ids with combination of all filters")
  void fetchBookingIdsWithMixedFiltersTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrGet();
    Booking booking = bookingDetails.booking();

    String firstName = BookerRandomUtils.randomOf(null, booking.firstName());
    String lastName = BookerRandomUtils.randomOf(null, booking.lastName());
    String checkIn = BookerRandomUtils.randomOf(null, booking.bookingDates().checkIn());
    String checkOut = BookerRandomUtils.randomOf(null, booking.bookingDates().checkOut());
    Response response = bookerClient.getBookingIds(firstName, lastName, checkIn, checkOut);

    bookingIdAssertion
        .assertThat(response)
        .statusIsOk()
        .body()
        .hasBookingId(bookingDetails.bookingId());

    bookingDetailsPool.push(bookingDetails);
  }
}
