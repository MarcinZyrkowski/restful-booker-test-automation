package org.example.client.health;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.example.api.BookerApi;
import org.example.assertion.common.ResponseAssertion;
import org.example.mapper.ResponseMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HealthClient {

  private final BookerApi bookerApi;
  private final ResponseMapper responseMapper;
  private final ResponseAssertion responseAssertion;

  @Step("Run health check client call")
  public String healthCheck() {
    Response response = bookerApi.healthCheck();
    responseAssertion.assertStatusCodeIsCreated(response);
    return responseMapper.mapToStringResponse(response);
  }
}
