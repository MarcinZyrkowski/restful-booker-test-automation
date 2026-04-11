package org.example.model.dto.response.auth;

import lombok.Builder;

@Builder
public record ErrorResponse(String reason) {}
