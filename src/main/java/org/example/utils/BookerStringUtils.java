package org.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookerStringUtils {

  private static final RandomStringUtils RANDOM = RandomStringUtils.secureStrong();

  public static String randomFullName() {
    return FakerUtils.FAKER.name().fullName();
  }

  public static String randomSentence() {
    return FakerUtils.FAKER.lorem().sentence();
  }

  public static String randomAlphaNumericSequence() {
    return randomAlphaNumericSequenceBetween(8, 16);
  }

  public static String randomAlphaNumericSequenceBetween(int minLen, int maxLen) {
    if (minLen > maxLen) {
      throw new IllegalArgumentException("minLen should be <= maxLen");
    }

    return RANDOM.nextAlphanumeric(minLen, maxLen + 1);
  }
}
