package org.example.client.token;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.example.api.BookerApi;
import org.example.assertion.common.ResponseAssert;
import org.example.mapper.ResponseMapper;
import org.example.model.service.dto.request.auth.User;
import org.example.model.service.dto.response.auth.Token;
import org.example.model.service.dto.response.common.ErrorResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenClient {

  private final BookerApi bookerApi;
  private final ResponseMapper responseMapper;

  @Step("Create token client call")
  public Token createToken(User user) {
    Response response = bookerApi.createToken(user);
    ResponseAssert.assertThat(response).isOk();
    return responseMapper.mapToTokenResponse(response);
  }

  @Step("Create token expecting error client call")
  public ErrorResponse createTokenExpectingError(User user) {
    Response response = bookerApi.createToken(user);
    // The API idiosyncratically returns 200 OK with an error body for failed auth attempts
    ResponseAssert.assertThat(response).isOk();
    return responseMapper.mapToErrorResponse(response);
  }
}
