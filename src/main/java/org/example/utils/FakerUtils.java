package org.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.datafaker.Faker;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FakerUtils {

  private static final Faker FAKER = new Faker();

  public static String generatePassword() {
    return FAKER.credentials().password();
  }

  public static String generateFirstName() {
    return FAKER.name().firstName();
  }

  public static String generateLastName() {
    return FAKER.name().lastName();
  }

  public static String generateFullName() {
    return FAKER.name().fullName();
  }

  public static String generateSentence() {
    return FAKER.lorem().sentence();
  }
}
