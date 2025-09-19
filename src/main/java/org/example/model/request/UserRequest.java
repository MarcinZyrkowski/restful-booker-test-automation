package org.example.model.request;

import lombok.Builder;

@Builder
public record UserRequest(
    String username,
    String password
) {

}
