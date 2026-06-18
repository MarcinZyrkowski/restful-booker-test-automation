package org.example.model.service.dto.request.auth;

import lombok.Builder;

@Builder
public record User(String username, String password) {}
