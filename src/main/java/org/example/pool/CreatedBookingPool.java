package org.example.pool;

import io.restassured.response.Response;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.factory.booking.BookingFactory;
import org.example.mapper.ResponseMapper;
import org.example.model.dto.common.Booking;
import org.example.model.dto.response.booking.CreatedBooking;
import org.example.steps.BookerClientSteps;
import org.example.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatedBookingPool {

  @Autowired
  private final BookerClientSteps bookerClientSteps;
  private final Set<CreatedBooking> createdBookingList =
      Collections.synchronizedSet(new HashSet<>());

  public void push(Response response) {
    CreatedBooking createdBooking = ResponseMapper.map(response).toCreateBookingResponse();
    push(createdBooking);
  }

  public void push(CreatedBooking createdBooking) {
    synchronized (createdBookingList) {
      createdBookingList.add(createdBooking);
    }
  }

  private CreatedBooking pop() {
    synchronized (createdBookingList) {
      Optional<CreatedBooking> createdBookingOptional = CollectionUtils.getRandomElement(
          createdBookingList);

      if (createdBookingOptional.isPresent()) {
        createdBookingList.remove(createdBookingOptional.get());
        return createdBookingOptional.get();
      }

      return null;
    }
  }

  public CreatedBooking popOrGet() {
    CreatedBooking createdBooking = pop();
    if (createdBooking == null) {
      Booking request = BookingFactory.getWithAllValidFields();
      return bookerClientSteps.createBooking(request);
    }

    return createdBooking;
  }

}
