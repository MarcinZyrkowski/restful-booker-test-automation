package org.example.pool;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.RequiredArgsConstructor;
import org.example.factory.booking.BookingFactory;
import org.example.mapper.ResponseMapper;
import org.example.model.service.dto.common.Booking;
import org.example.model.service.dto.response.booking.BookingDetails;
import org.example.steps.BookerClientSteps;
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
  private final Queue<BookingDetails> pool = new ConcurrentLinkedQueue<>();

  @Step("Push booking details (response) to the reusable pool")
  public void push(Response response) {
    BookingDetails bookingDetails = responseMapper.mapToCreateBookingResponse(response);
    push(bookingDetails);
  }

  @Step("Push booking details to the reusable pool")
  public void push(BookingDetails bookingDetails) {
    pool.offer(bookingDetails);
  }

  private BookingDetails pop() {
    return pool.poll();
  }

  @Step("Get existing booking details from pool or create a new one")
  public BookingDetails popOrCreate() {
    BookingDetails bookingDetails = pop();
    if (bookingDetails == null) {
      Booking request = bookingFactory.getWithAllValidFields();
      return bookerClientSteps.createBooking(request);
    }

    return bookingDetails;
  }
}
