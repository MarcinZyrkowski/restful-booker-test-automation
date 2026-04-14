package org.example.pool;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.factory.booking.BookingFactory;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.common.Booking;
import org.example.model.dto.response.booking.BookingDetails;
import org.example.steps.BookerClientSteps;
import org.example.utils.CollectionUtils;
import org.springframework.stereotype.Component;

/**
 * Thread-safe pool for managing BookingDetails across tests.
 *
 * <p>Note: This pool manages a shared collection of booking details. To prevent flaky tests and
 * race conditions:
 *
 * <ul>
 *   <li>All bookings are immutable records and should not be modified after creation
 *   <li>The pool uses synchronization to ensure thread-safe add/remove operations
 *   <li>Tests should push bookings back to the pool to allow reuse
 *   <li>Avoid long-lived references to popped bookings
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class BookingDetailsPool {

  private final ResponseMapper responseMapper;
  private final BookerClientSteps bookerClientSteps;
  private final BookingFactory bookingFactory;
  private final Set<BookingDetails> bookingDetailsList =
      Collections.synchronizedSet(new HashSet<>());

  public void push(Response response) {
    BookingDetails bookingDetails = responseMapper.mapToCreateBookingResponse(response);
    push(bookingDetails);
  }

  public void push(BookingDetails bookingDetails) {
    synchronized (bookingDetailsList) {
      bookingDetailsList.add(bookingDetails);
    }
  }

  private BookingDetails pop() {
    synchronized (bookingDetailsList) {
      Optional<BookingDetails> createdBookingOptional =
          CollectionUtils.getRandomElement(bookingDetailsList);

      if (createdBookingOptional.isPresent()) {
        bookingDetailsList.remove(createdBookingOptional.get());
        return createdBookingOptional.get();
      }
      return null;
    }
  }

  @Step("Get existing booking details from pool or create a new one")
  public BookingDetails popOrGet() {
    BookingDetails bookingDetails = pop();
    if (bookingDetails == null) {
      synchronized (bookingDetailsList) {
        Booking request = bookingFactory.getWithAllValidFields();
        return bookerClientSteps.createBooking(request);
      }
    }

    return bookingDetails;
  }
}
