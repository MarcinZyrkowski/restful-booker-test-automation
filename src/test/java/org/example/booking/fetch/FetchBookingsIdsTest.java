package org.example.booking.fetch;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import java.time.LocalDate;
import java.util.List;
import org.example.assertion.booking.BookingIdListAssert;
import org.example.client.booking.FetchBookingClient;
import org.example.config.SpringConfig;
import org.example.generator.DateGenerator;
import org.example.mapper.DateMapper;
import org.example.model.service.dto.common.Booking;
import org.example.model.service.dto.response.booking.BookingDetails;
import org.example.model.service.dto.response.booking.BookingId;
import org.example.pool.BookingDetailsPool;
import org.example.tags.Bug;
import org.example.tags.Regression;
import org.example.tracking.Bugs;
import org.example.utils.BookerRandomUtils;
import org.example.utils.BookerStringUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Severity(SeverityLevel.NORMAL)
@Epic("Booking API")
@Feature("Booking Management")
@Story("Fetch Booking IDs")
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
    // Send request to fetch all booking IDs without filters
    List<BookingId> response = fetchBookingClient.getBookingIds(null, null, null, null);

    // Verify the returned list is not empty
    BookingIdListAssert.assertThat(response).isNotEmpty();
  }

  @Test
  @DisplayName("Fetch booking ids with filter by first name")
  void fetchBookingIdsWithFilterByFirstNameTest() {
    // Retrieve an existing booking from the pool
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    Booking booking = bookingDetails.booking();

    // Send request to fetch booking IDs filtered by first name
    List<BookingId> response =
        fetchBookingClient.getBookingIds(booking.firstName(), null, null, null);

    // Verify the retrieved list contains the pooled booking's ID
    BookingIdListAssert.assertThat(response).containsBookingId(bookingDetails.bookingId());

    // Return the booking to the pool
    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Fetch booking ids with filter by last name")
  void fetchBookingIdsWithFilterByLastNameTest() {
    // Retrieve an existing booking from the pool
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    Booking booking = bookingDetails.booking();

    // Send request to fetch booking IDs filtered by last name
    List<BookingId> response =
        fetchBookingClient.getBookingIds(null, booking.lastName(), null, null);

    // Verify the retrieved list contains the pooled booking's ID
    BookingIdListAssert.assertThat(response).containsBookingId(bookingDetails.bookingId());

    // Return the booking to the pool
    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Fetch booking ids with filter by non-existent first name")
  void fetchBookingIdsWithFilterByNonExistentFirstNameTest() {
    // Generate a random non-existent first name
    String nonExistentFirstName = BookerStringUtils.randomAlphaNumericSequence();

    // Send request to fetch booking IDs filtered by the non-existent first name
    List<BookingId> response =
        fetchBookingClient.getBookingIds(nonExistentFirstName, null, null, null);

    // Verify the retrieved list is empty
    BookingIdListAssert.assertThat(response).isEmpty();
  }

  @Test
  @DisplayName("Fetch booking ids with filter by non-existent last name")
  void fetchBookingIdsWithFilterByNonExistentLastNameTest() {
    // Generate a random non-existent last name
    String nonExistentLastName = BookerStringUtils.randomAlphaNumericSequence();

    // Send request to fetch booking IDs filtered by the non-existent last name
    List<BookingId> response =
        fetchBookingClient.getBookingIds(null, nonExistentLastName, null, null);

    // Verify the retrieved list is empty
    BookingIdListAssert.assertThat(response).isEmpty();
  }

  @Issue(value = Bugs.CHECK_IN_BUG)
  @Disabled(value = "Skipped because of bug: " + Bugs.CHECK_IN_BUG)
  @Bug
  @Test
  @DisplayName("Fetch booking ids with filter by check in date (equal to booking check in date)")
  void fetchBookingIdsWithFilterByCheckInDateTest() {
    // Retrieve an existing booking from the pool
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    Booking booking = bookingDetails.booking();

    // Send request to fetch booking IDs filtered by the exact check-in date
    List<BookingId> response =
        fetchBookingClient.getBookingIds(null, null, booking.bookingDates().checkIn(), null);

    // Verify the retrieved list contains the pooled booking's ID
    BookingIdListAssert.assertThat(response).containsBookingId(bookingDetails.bookingId());

    // Return the booking to the pool
    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName(
      "Fetch booking ids with filter by check in date (earlier than booking check in date)")
  void fetchBookingIdsWithFilterByCheckInDateEarlierThanTest() {
    // Retrieve an existing booking from the pool
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    Booking booking = bookingDetails.booking();

    // Calculate a date earlier than the booking's check-in date
    LocalDate bookingCheckIn = dateMapper.mapStringToLocalDate(booking.bookingDates().checkIn());
    LocalDate filterCheckIn = DateGenerator.getRandomDateBefore(bookingCheckIn, 50);

    // Send request to fetch booking IDs filtered by the earlier check-in date
    List<BookingId> response =
        fetchBookingClient.getBookingIds(null, null, filterCheckIn.toString(), null);

    // Verify the retrieved list contains the pooled booking's ID
    BookingIdListAssert.assertThat(response).containsBookingId(bookingDetails.bookingId());

    // Return the booking to the pool
    bookingDetailsPool.push(bookingDetails);
  }

  @Test
  @DisplayName("Fetch booking ids with filter by checkout date (equal to booking checkout date)")
  void fetchBookingIdsWithFilterByCheckOutDateTest() {
    // Retrieve an existing booking from the pool
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    Booking booking = bookingDetails.booking();

    // Send request to fetch booking IDs filtered by the exact check-out date
    List<BookingId> response =
        fetchBookingClient.getBookingIds(null, null, null, booking.bookingDates().checkOut());

    // Verify the retrieved list contains the pooled booking's ID
    BookingIdListAssert.assertThat(response).containsBookingId(bookingDetails.bookingId());

    // Return the booking to the pool
    bookingDetailsPool.push(bookingDetails);
  }

  @Issue(value = Bugs.CHECK_OUT_BUG)
  @Disabled(value = "Skipped because of bug: " + Bugs.CHECK_OUT_BUG)
  @Bug
  @Test
  @DisplayName(
      "Fetch booking ids with filter by checkout date (earlier than booking checkout date)")
  void fetchBookingIdsWithFilterByCheckOutDateLessThanTest() {
    // Retrieve an existing booking from the pool
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    Booking booking = bookingDetails.booking();

    // Calculate a date earlier than the booking's check-out date
    LocalDate bookingCheckOut = dateMapper.mapStringToLocalDate(booking.bookingDates().checkOut());
    LocalDate filterCheckOut = DateGenerator.getRandomDateBefore(bookingCheckOut, 50);

    // Send request to fetch booking IDs filtered by the earlier check-out date
    List<BookingId> response =
        fetchBookingClient.getBookingIds(null, null, null, filterCheckOut.toString());

    // Verify the retrieved list contains the pooled booking's ID
    BookingIdListAssert.assertThat(response).containsBookingId(bookingDetails.bookingId());

    // Return the booking to the pool
    bookingDetailsPool.push(bookingDetails);
  }

  @Issue(value = Bugs.CHECK_IN_BUG)
  @Disabled(value = "Skipped because of bug: " + Bugs.CHECK_IN_BUG)
  @Bug
  @Test
  @DisplayName("Fetch booking ids with combination of all filters")
  void fetchBookingIdsWithMixedFiltersTest() {
    // Retrieve an existing booking from the pool
    BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
    Booking booking = bookingDetails.booking();

    // Randomly select between the booking's actual details or null for each filter
    String firstName = BookerRandomUtils.randomOf(null, booking.firstName());
    String lastName = BookerRandomUtils.randomOf(null, booking.lastName());
    String checkIn = BookerRandomUtils.randomOf(null, booking.bookingDates().checkIn());
    String checkOut = BookerRandomUtils.randomOf(null, booking.bookingDates().checkOut());

    // Send request to fetch booking IDs using the mixed filters
    List<BookingId> response =
        fetchBookingClient.getBookingIds(firstName, lastName, checkIn, checkOut);

    // Verify the retrieved list contains the pooled booking's ID
    BookingIdListAssert.assertThat(response).containsBookingId(bookingDetails.bookingId());

    // Return the booking to the pool
    bookingDetailsPool.push(bookingDetails);
  }
}
