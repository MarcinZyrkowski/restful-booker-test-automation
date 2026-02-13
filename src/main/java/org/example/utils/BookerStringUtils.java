package org.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookerStringUtils {

  public static final RandomStringUtils RANDOM = RandomStringUtils.secureStrong();

  public static String randomFullName() {
    return FakerUtils.FAKER.name().fullName();
  }

  public static String randomSentence() {
    return FakerUtils.FAKER.lorem().sentence();
  }

}
