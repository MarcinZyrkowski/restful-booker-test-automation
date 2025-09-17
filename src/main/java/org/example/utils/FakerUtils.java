package org.example.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.datafaker.Faker;
import net.datafaker.service.FakeValuesService;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FakerUtils {

  @Getter
  private static final Faker FAKER = new Faker();

  @Getter
  private static final FakeValuesService FAKE_VALUES_SERVICE = new FakeValuesService();

}
