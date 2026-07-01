package org.example.booking.fetch;

import io.qameta.allure.Issue;
import io.restassured.response.Response;
import java.time.LocalDate;
import org.example.assertion.booking.BookingIdAssertion;
import org.example.client.BookerClient;
import org.example.config.SpringConfig;
import org.example.generator.DateTimesGenerator;
import org.example.mapper.DateMapper;
import org.example.model.service.dto.common.Booking;
import org.example.model.service.dto.response.booking.BookingDetails;
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

  @Autowired private BookerClient bookerClient;
  @Autowired private BookingIdAssertion bookingIdAssertion;
  @Autowired private BookingDetailsPool bookingDetailsPool;
  @Autowired private DateMapper dateMapper;

  @Test
  @DisplayName("Fetch all booking ids")
  void fetchAllBookingIdsTest() {
    Response response = bookerClient.getBookingIds(null, null, null, null);

    bookingIdAssertion.assertBookingIdsAreNotEmpty(response);
  }

  @Test
  @DisplayName("Fetch booking ids with filter by first name")
  void fetchBookingIdsWithFilterByFirstNameTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    Booking booking = bookingDetails.booking();

    Response response = bookerClient.getBookingIds(booking.firstName(), null, null, null);

    bookingIdAssertion.assertBookingIdsContainsBookingId(response, bookingDetails.bookingId());

    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Fetch booking ids with filter by last name")
  void fetchBookingIdsWithFilterByLastNameTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    Booking booking = bookingDetails.booking();

    Response response = bookerClient.getBookingIds(null, booking.lastName(), null, null);

    bookingIdAssertion.assertBookingIdsContainsBookingId(response, bookingDetails.bookingId());

    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Fetch booking ids with filter by non-existent first name")
  void fetchBookingIdsWithFilterByNonExistentFirstNameTest() {
    String nonExistentFirstName = BookerStringUtils.randomAlphaNumericSequence();

    Response response = bookerClient.getBookingIds(nonExistentFirstName, null, null, null);

    bookingIdAssertion.assertBookingIdsIsEmpty(response);
  }

  @Test
  @DisplayName("Fetch booking ids with filter by non-existent last name")
  void fetchBookingIdsWithFilterByNonExistentLastNameTest() {
    String nonExistentLastName = BookerStringUtils.randomAlphaNumericSequence();

    Response response = bookerClient.getBookingIds(null, nonExistentLastName, null, null);

    bookingIdAssertion.assertBookingIdsIsEmpty(response);
  }

  @Issue(value = Bugs.CHECK_IN_BUG)
  @Disabled(value = "Skipped because of bug: " + Bugs.CHECK_IN_BUG)
  @Test
  @DisplayName("Fetch booking ids with filter by check in date (equal to booking check in date)")
  void fetchBookingIdsWithFilterByCheckInDateTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    Booking booking = bookingDetails.booking();

    Response response =
        bookerClient.getBookingIds(null, null, booking.bookingDates().checkIn(), null);

    bookingIdAssertion.assertBookingIdsContainsBookingId(response, bookingDetails.bookingId());

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

    Response response = bookerClient.getBookingIds(null, null, filterCheckIn.toString(), null);

    bookingIdAssertion.assertBookingIdsContainsBookingId(response, bookingDetails.bookingId());

    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Fetch booking ids with filter by checkout date (equal to booking checkout date)")
  void fetchBookingIdsWithFilterByCheckOutDateTest() {
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    Booking booking = bookingDetails.booking();

    Response response =
        bookerClient.getBookingIds(null, null, null, booking.bookingDates().checkOut());

    bookingIdAssertion.assertBookingIdsContainsBookingId(response, bookingDetails.bookingId());

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

    Response response = bookerClient.getBookingIds(null, null, null, filterCheckOut.toString());

    bookingIdAssertion.assertBookingIdsContainsBookingId(response, bookingDetails.bookingId());

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
    Response response = bookerClient.getBookingIds(firstName, lastName, checkIn, checkOut);

    bookingIdAssertion.assertBookingIdsContainsBookingId(response, bookingDetails.bookingId());

    bookingDetailsPool.push(bookingDetails);
  }
}
