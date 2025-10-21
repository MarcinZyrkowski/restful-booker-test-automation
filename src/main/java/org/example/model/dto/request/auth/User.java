package org.example.model.dto.request.auth;

import lombok.Builder;

@Builder
public record User(String username, String password) {}
