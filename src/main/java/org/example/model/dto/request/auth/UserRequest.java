package org.example.model.dto.request.auth;

import lombok.Builder;

@Builder
public record UserRequest(
    String username,
    String password
) {

}
