package org.example.assertion;

import org.assertj.core.api.Assertions;
import org.springframework.stereotype.Component;

@Component
public class AssertionUtils {

  public <T> void assertEqualsIgnoringOrderAndFields(
      T actual, T expected, String... ignorableFields) {
    Assertions.assertThat(actual)
        .usingRecursiveComparison()
        .ignoringCollectionOrder()
        .ignoringFields(ignorableFields)
        .isEqualTo(expected);
  }

  public <T> void assertEqualsIgnoringOrder(T actual, T expected) {
    // no fields to ignore
    assertEqualsIgnoringOrderAndFields(actual, expected);
  }

  public <T> void assertElementIsInCollectionIgnoringOrderAndFields(
      T element, Iterable<T> collection, String... ignorableFields) {
    Assertions.assertThat(element)
        .usingRecursiveComparison()
        .ignoringCollectionOrder()
        .ignoringFields(ignorableFields)
        .isIn(collection);
  }

  public <T> void assertElementsAreInCollectionIgnoringOrderAndFields(
      Iterable<T> elements, Iterable<T> collection, String... ignorableFields) {
    elements.forEach(
        e -> assertElementIsInCollectionIgnoringOrderAndFields(e, collection, ignorableFields));
  }

  public <T> void assertEquals(T actual, T expected) {
    Assertions.assertThat(actual).isEqualTo(expected);
  }
}
