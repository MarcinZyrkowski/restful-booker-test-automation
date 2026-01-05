package org.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.datafaker.Faker;
import net.datafaker.service.FakeValuesService;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FakerUtils {

  public static final Faker FAKER = new Faker();

  public static final FakeValuesService FAKE_VALUES_SERVICE = new FakeValuesService();
}
