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

  public static long randomLong(long includedMin, long excludedMax) {
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

  public static int randomInt(int includedMin, int excludedMax) {
    if (includedMin > excludedMax) {
      throw new IllegalArgumentException("includedMin should be <= excludedMax");
    }

    if (includedMin == excludedMax) {
      return includedMin;
    }

    int range = excludedMax - includedMin; // always positive here
    int offset = RANDOM.randomInt(0, range); // [0, range)
    return (includedMin + offset);
  }

  public static String randomLongAsString(long includedMin, long excludedMax) {
    return String.valueOf(randomLong(includedMin, excludedMax));
  }

  public static boolean randomBoolean() {
    return RANDOM.randomBoolean();
  }
}
