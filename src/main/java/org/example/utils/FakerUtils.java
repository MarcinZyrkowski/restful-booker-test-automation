package org.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.datafaker.Faker;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FakerUtils {

  private static final ThreadLocal<Faker> FAKER = ThreadLocal.withInitial(Faker::new);

  public static String generatePassword() {
    return FAKER.get().credentials().password();
  }

  public static String generateFirstName() {
    return FAKER.get().name().firstName();
  }

  public static String generateLastName() {
    return FAKER.get().name().lastName();
  }

  public static String generateFullName() {
    return FAKER.get().name().fullName();
  }

  public static String generateSentence() {
    return FAKER.get().lorem().sentence();
  }
}
