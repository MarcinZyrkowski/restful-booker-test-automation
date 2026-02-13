package org.example.tracking;

public interface Bugs {
  String CHECK_IN_BUG =
      "Check in booking filter should return all bookings greater than or equal than provided date. doesn't work with strict equals";
  String CHECK_OUT_BUG = "Check out booking filter should return all bookings greater than or equal than provided date. doesn't work with strict greater than";
}
