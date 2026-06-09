package org.example.model.service.dto.response.auth;

import lombok.Builder;

@Builder
public record ErrorResponse(String reason) {}
