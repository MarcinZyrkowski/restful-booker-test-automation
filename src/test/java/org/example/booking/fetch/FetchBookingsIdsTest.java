package org.example.booking.fetch;

import io.qameta.allure.Issue;
import java.time.LocalDate;
import java.util.List;
import org.example.assertion.booking.BookingIdListAssert;
import org.example.client.booking.FetchBookingClient;
import org.example.config.SpringConfig;
import org.example.generator.DateTimesGenerator;
import org.example.mapper.DateMapper;
import org.example.model.service.dto.common.Booking;
import org.example.model.service.dto.response.booking.BookingDetails;
import org.example.model.service.dto.response.booking.BookingId;
import org.example.pool.BookingDetailsPool;
import org.example.tags.Regression;
import org.example.tracking.Bugs;
import org.example.utils.BookerRandomUtils;
import org.example.utils.BookerStringUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Regression
@SpringBootTest(classes = SpringConfig.class)
@DisplayName("Fetch Booking Ids")
class FetchBookingsIdsTest {

  @Autowired private BookingDetailsPool bookingDetailsPool;
  @Autowired private DateMapper dateMapper;
  @Autowired private FetchBookingClient fetchBookingClient;

  @Test
  @DisplayName("Fetch all booking ids")
  void fetchAllBookingIdsTest() {
    List<BookingId> response = fetchBookingClient.getBookingIds(null, null, null, null);

    BookingIdListAssert.assertThat(response).isNotEmpty();
  }

  @Test
  @DisplayName("Fetch booking ids with filter by first name")
  void fetchBookingIdsWithFilterByFirstNameTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    Booking booking = bookingDetails.booking();

    List<BookingId> response =
        fetchBookingClient.getBookingIds(booking.firstName(), null, null, null);

    BookingIdListAssert.assertThat(response).containsBookingId(bookingDetails.bookingId());

    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Fetch booking ids with filter by last name")
  void fetchBookingIdsWithFilterByLastNameTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    Booking booking = bookingDetails.booking();

    List<BookingId> response =
        fetchBookingClient.getBookingIds(null, booking.lastName(), null, null);

    BookingIdListAssert.assertThat(response).containsBookingId(bookingDetails.bookingId());

    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Fetch booking ids with filter by non-existent first name")
  void fetchBookingIdsWithFilterByNonExistentFirstNameTest() {
    String nonExistentFirstName = BookerStringUtils.randomAlphaNumericSequence();

    List<BookingId> response =
        fetchBookingClient.getBookingIds(nonExistentFirstName, null, null, null);

    BookingIdListAssert.assertThat(response).isEmpty();
  }

  @Test
  @DisplayName("Fetch booking ids with filter by non-existent last name")
  void fetchBookingIdsWithFilterByNonExistentLastNameTest() {
    String nonExistentLastName = BookerStringUtils.randomAlphaNumericSequence();

    List<BookingId> response =
        fetchBookingClient.getBookingIds(null, nonExistentLastName, null, null);

    BookingIdListAssert.assertThat(response).isEmpty();
  }

  @Issue(value = Bugs.CHECK_IN_BUG)
  @Disabled(value = "Skipped because of bug: " + Bugs.CHECK_IN_BUG)
  @Test
  @DisplayName("Fetch booking ids with filter by check in date (equal to booking check in date)")
  void fetchBookingIdsWithFilterByCheckInDateTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    Booking booking = bookingDetails.booking();

    List<BookingId> response =
        fetchBookingClient.getBookingIds(null, null, booking.bookingDates().checkIn(), null);

    BookingIdListAssert.assertThat(response).containsBookingId(bookingDetails.bookingId());

    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName(
      "Fetch booking ids with filter by check in date (earlier than booking check in date)")
  void fetchBookingIdsWithFilterByCheckInDateEarlierThanTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    Booking booking = bookingDetails.booking();

    LocalDate bookingCheckIn = dateMapper.mapStringToLocalDate(booking.bookingDates().checkIn());
    LocalDate filterCheckIn = DateTimesGenerator.getRandomDateBefore(bookingCheckIn, 50);

    List<BookingId> response =
        fetchBookingClient.getBookingIds(null, null, filterCheckIn.toString(), null);

    BookingIdListAssert.assertThat(response).containsBookingId(bookingDetails.bookingId());

    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Fetch booking ids with filter by checkout date (equal to booking checkout date)")
  void fetchBookingIdsWithFilterByCheckOutDateTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    Booking booking = bookingDetails.booking();

    List<BookingId> response =
        fetchBookingClient.getBookingIds(null, null, null, booking.bookingDates().checkOut());

    BookingIdListAssert.assertThat(response).containsBookingId(bookingDetails.bookingId());

    bookingDetailsPool.push(bookingDetails);
  }

  @Issue(value = Bugs.CHECK_OUT_BUG)
  @Disabled(value = "Skipped because of bug: " + Bugs.CHECK_OUT_BUG)
  @Test
  @DisplayName(
      "Fetch booking ids with filter by checkout date (earlier than booking checkout date)")
  void fetchBookingIdsWithFilterByCheckOutDateLessThanTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    Booking booking = bookingDetails.booking();

    LocalDate bookingCheckOut = dateMapper.mapStringToLocalDate(booking.bookingDates().checkOut());
    LocalDate filterCheckOut = DateTimesGenerator.getRandomDateBefore(bookingCheckOut, 50);

    List<BookingId> response =
        fetchBookingClient.getBookingIds(null, null, null, filterCheckOut.toString());

    BookingIdListAssert.assertThat(response).containsBookingId(bookingDetails.bookingId());

    bookingDetailsPool.push(bookingDetails);
  }

  @Issue(value = Bugs.CHECK_IN_BUG)
  @Disabled(value = "Skipped because of bug: " + Bugs.CHECK_IN_BUG)
  @Test
  @DisplayName("Fetch booking ids with combination of all filters")
  void fetchBookingIdsWithMixedFiltersTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    Booking booking = bookingDetails.booking();

    String firstName = BookerRandomUtils.randomOf(null, booking.firstName());
    String lastName = BookerRandomUtils.randomOf(null, booking.lastName());
    String checkIn = BookerRandomUtils.randomOf(null, booking.bookingDates().checkIn());
    String checkOut = BookerRandomUtils.randomOf(null, booking.bookingDates().checkOut());
    List<BookingId> response =
        fetchBookingClient.getBookingIds(firstName, lastName, checkIn, checkOut);

    BookingIdListAssert.assertThat(response).containsBookingId(bookingDetails.bookingId());

    bookingDetailsPool.push(bookingDetails);
  }
}
