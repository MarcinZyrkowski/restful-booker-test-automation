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

@Component
@RequiredArgsConstructor
public class BookingDetailsPool {

  private final ResponseMapper responseMapper;
  private final BookerClientSteps bookerClientSteps;
  private final Set<BookingDetails> bookingDetailsList =
      Collections.synchronizedSet(new HashSet<>());

  public void push(Response response) {
    BookingDetails bookingDetails = responseMapper.map(response).toCreateBookingResponse();
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
      Booking request = BookingFactory.getWithAllValidFields();
      return bookerClientSteps.createBooking(request);
    }

    return bookingDetails;
  }
}
