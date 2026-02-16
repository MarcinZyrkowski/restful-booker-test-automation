package org.example.mapper;

import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class DateMapper {

  public LocalDate mapStringToLocalDate(String dateString) {
    return LocalDate.parse(dateString);
  }
}
