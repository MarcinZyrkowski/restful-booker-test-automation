package org.example.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionUtils {

  public static <T> Optional<T> getRandomElement(@NonNull Collection<T> collection) {

    return collection.stream()
        .skip(BookerRandomUtils.RANDOM.randomInt(0, collection.size()))
        .findAny();
  }

  public static <K, V> Map<K, V> filterNonNullValues(Map<K, V> map) {
    return map.entrySet()
        .stream()
        .filter(entry -> entry.getValue() != null)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

}
