package org.example.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjMapper {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  public static String asJson(Object object) {
    try {
      return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (Exception e) {
      return "serialization error: " + e.getMessage();
    }
  }
}
