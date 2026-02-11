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

}
