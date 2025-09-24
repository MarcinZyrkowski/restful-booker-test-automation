package org.example.model.enums.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StringResponseBody {

  CREATED("Created"),
  NOT_FOUND("Not Found"),
  INTERNAL_SERVER_ERROR("Internal Server Error");

  private final String body;

}
