package org.example.utils;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class BookerRandomUtils {

  private static final RandomUtils RANDOM = RandomUtils.secureStrong();

  public static <T> T randomNullOrValue(T value) {
    return RANDOM.randomBoolean() ? null : value;
  }

  @SafeVarargs
  public static <T> T randomOf(T... values) {
    return values[RANDOM.randomInt(0, values.length)];
  }

  /**
   * Generates a random integer between {@code includedMin} (included) and {@code excludedMax}
   * (included).
   *
   * <p>Supports negative ranges, e.g. {@code (-6, -1)}.
   *
   * @throws IllegalArgumentException if {@code includedMin > excludedMax}
   */
  public static long randomNumber(long includedMin, long excludedMax) {
    if (includedMin > excludedMax) {
      throw new IllegalArgumentException("includedMin should be <= excludedMax");
    }

    if (includedMin == excludedMax) {
      return includedMin;
    }

    long range = excludedMax - includedMin; // always positive here
    long offset = RANDOM.randomLong(0, range); // [0, range)
    return (includedMin + offset);
  }

  public static String randomNumberAsString(long includedMin, long excludedMax) {
    return String.valueOf(randomNumber(includedMin, excludedMax));
  }

  public static boolean randomBoolean() {
    return RANDOM.randomBoolean();
  }
}
