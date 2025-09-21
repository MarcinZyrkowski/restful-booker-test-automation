package org.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumUtils {

  public static <T extends Enum<T>> T getRandomValue(Class<T> enumClass) {
    T[] values = enumClass.getEnumConstants();

    if (values == null || values.length == 0) {
      throw new IllegalArgumentException("Enum class must have at least one constant");
    }

    int randomIndex = BookerRandomUtils.RANDOM.randomInt(0, values.length);
    return values[randomIndex];
  }

}
