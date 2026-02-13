package org.example.model.enums.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StringResponseBody {
  CREATED("Created"),
  FORBIDDEN("Forbidden"),
  NOT_FOUND("Not Found"),
  METHOD_NOT_ALLOWED("Method Not Allowed"),
  INTERNAL_SERVER_ERROR("Internal Server Error");

  private final String body;
}
