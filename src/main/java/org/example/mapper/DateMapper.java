package org.example.mapper;

import java.time.LocalDate;

public class DateMapper {

  public static LocalDate mapStringToLocalDate(String dateString) {
    return LocalDate.parse(dateString);
  }
}
