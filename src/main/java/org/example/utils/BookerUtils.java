package org.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookerUtils {

  public static <T> T nonNullValueOrDefault(T value, T defaultValue) {
    return value != null ? value : defaultValue;
  }
}
