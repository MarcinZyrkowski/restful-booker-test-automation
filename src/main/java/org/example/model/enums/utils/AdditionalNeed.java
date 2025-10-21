package org.example.model.enums.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.utils.EnumUtils;

@Getter
@RequiredArgsConstructor
public enum AdditionalNeed {
  BREAKFAST("Breakfast"),
  LUNCH("Lunch"),
  DINNER("Dinner"),
  AIRPORT_SHUTTLE("Airport Shuttle"),
  PARKING("Parking"),
  SPA_ACCESS("Spa Access"),
  GYM_ACCESS("Gym Access"),
  SWIMMING_POOL_ACCESS("Swimming Pool Access"),
  WIFI("WiFi"),
  EXTRA_BED("Extra Bed"),
  BABY_COT("Baby Cot"),
  ROOM_SERVICE("Room Service"),
  LATE_CHECKOUT("Late Checkout"),
  EARLY_CHECKIN("Early Check-in"),
  MINI_BAR("Mini Bar"),
  LAUNDRY_SERVICE("Laundry Service"),
  DRY_CLEANING("Dry Cleaning"),
  PET_FEE("Pet Fee"),
  CAR_RENTAL("Car Rental"),
  TOUR_GUIDE("Tour Guide"),
  BUSINESS_CENTER("Business Center"),
  CONFERENCE_ROOM("Conference Room"),
  BEACH_ACCESS("Beach Access"),
  SKI_PASS("Ski Pass");

  private final String value;

  public static AdditionalNeed getRandom() {
    return EnumUtils.getRandomValue(AdditionalNeed.class);
  }
}
