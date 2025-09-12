package org.example.assertion;

import lombok.NoArgsConstructor;
import org.assertj.core.api.Assertions;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class AssertionUtils {

  public static <T> void equalsIgnoringOrderAndFields(
      T actual,
      T expected,
      String... ignorableFields
  ) {
    Assertions.assertThat(actual)
        .usingRecursiveComparison()
        .ignoringCollectionOrder()
        .ignoringFields(ignorableFields)
        .isEqualTo(expected);
  }

  public static <T> void equalsIgnoringOrder(T actual, T expected) {
    // no fields to ignore
    equalsIgnoringOrderAndFields(actual, expected);
  }

  public static <T> void elementIsInCollectionIgnoringOrderAndFields(
      T element,
      Iterable<T> collection,
      String... ignorableFields
  ) {
    Assertions.assertThat(element)
        .usingRecursiveComparison()
        .ignoringCollectionOrder()
        .ignoringFields(ignorableFields)
        .isIn(collection);
  }


  public static <T> void elementsAreInCollectionIgnoringOrderAndFields(
      Iterable<T> elements,
      Iterable<T> collection,
      String... ignorableFields
  ) {
    elements
        .forEach(e -> elementIsInCollectionIgnoringOrderAndFields(e, collection, ignorableFields));
  }

}
