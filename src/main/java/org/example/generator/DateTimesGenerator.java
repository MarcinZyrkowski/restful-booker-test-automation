package org.example.generator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.utils.BookerRandomUtils;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimesGenerator {

  public static LocalDate getRandomFutureDate() {
    LocalDate now = LocalDate.now();
    LocalDate twoYearsLater = now.plusYears(2);

    long minDay = now.toEpochDay();
    long maxDay = twoYearsLater.toEpochDay();

    long randomDay = BookerRandomUtils.RANDOM.randomLong(minDay + 1, maxDay + 1);

    return LocalDate.ofEpochDay(randomDay);
  }

  public static LocalDate getRandomDateAfter(LocalDate startDate, int maxDaysLater) {
    if (maxDaysLater <= 0) {
      throw new IllegalArgumentException("maxDaysLater must be greater than 0");
    }

    long minDay = startDate.toEpochDay() + 1;
    long maxDay = startDate.toEpochDay() + maxDaysLater;

    long randomDay = BookerRandomUtils.RANDOM.randomLong(minDay + 1, maxDay + 1);

    return LocalDate.ofEpochDay(randomDay);
  }

  public static LocalDate getRandomDateBefore(LocalDate startDate, int maxDaysBefore) {
    if (maxDaysBefore <= 0) {
      throw new IllegalArgumentException("maxDaysBefore must be greater than 0");
    }

    long maxDay = startDate.toEpochDay() - 1;
    long minDay = maxDay - maxDaysBefore;

    long randomDay = BookerRandomUtils.RANDOM.randomLong(minDay + 1, maxDay);
    return LocalDate.ofEpochDay(randomDay);
  }
}
