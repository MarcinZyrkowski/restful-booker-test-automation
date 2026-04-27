package org.example.utils;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class BookerRandomUtils {

  public static final RandomUtils RANDOM = RandomUtils.secureStrong();

  public static <T> T randomNullOrValue(T value) {
    return RANDOM.randomBoolean() ? null : value;
  }

  @SafeVarargs
  public static <T> T randomOf(T... values) {
    return values[RANDOM.randomInt(0, values.length)];
  }

  /**
   * Generates a random integer between {@code includedMin} (included) and {@code includedMax}
   * (included).
   *
   * <p>Supports negative ranges, e.g. {@code (-6, -1)}.
   *
   * @throws IllegalArgumentException if {@code includedMin > includedMax}
   */
  public static int randomNumber(int includedMin, int includedMax) {
    if (includedMin > includedMax) {
      throw new IllegalArgumentException("includedMin should be <= includedMax");
    }

    if (includedMin == includedMax) {
      return includedMin;
    }

    long range = (long) includedMax - (long) includedMin + 1; // always positive here
    long offset = RANDOM.randomLong(0, range); // [0, range)
    return (int) (includedMin + offset);
  }
}
