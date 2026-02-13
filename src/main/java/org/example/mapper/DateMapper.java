package org.example.mapper;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DateMapper {

  public LocalDate mapStringToLocalDate(String dateString) {
    return LocalDate.parse(dateString);
  }
}
