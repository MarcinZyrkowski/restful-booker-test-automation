package org.example.assertion;

import org.assertj.core.api.Assertions;
import org.springframework.stereotype.Component;

@Component
public class AssertionUtils {

  public <T> void equalsIgnoringOrderAndFields(T actual, T expected, String... ignorableFields) {
    Assertions.assertThat(actual)
        .usingRecursiveComparison()
        .ignoringCollectionOrder()
        .ignoringFields(ignorableFields)
        .isEqualTo(expected);
  }

  public <T> void equalsIgnoringOrder(T actual, T expected) {
    // no fields to ignore
    equalsIgnoringOrderAndFields(actual, expected);
  }

  public <T> void elementIsInCollectionIgnoringOrderAndFields(
      T element, Iterable<T> collection, String... ignorableFields) {
    Assertions.assertThat(element)
        .usingRecursiveComparison()
        .ignoringCollectionOrder()
        .ignoringFields(ignorableFields)
        .isIn(collection);
  }

  public <T> void elementsAreInCollectionIgnoringOrderAndFields(
      Iterable<T> elements, Iterable<T> collection, String... ignorableFields) {
    elements.forEach(
        e -> elementIsInCollectionIgnoringOrderAndFields(e, collection, ignorableFields));
  }
}
