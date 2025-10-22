package org.example;

import org.example.assertion.response.StringResponseAssertion;
import org.example.assertion.response.auth.TokenResponseAssertion;
import org.example.assertion.response.booking.BookingAssertion;
import org.example.assertion.response.booking.BookingDetailsAssertion;
import org.example.assertion.response.booking.BookingIdAssertion;
import org.example.client.BookerClient;
import org.example.config.SpringConfig;
import org.example.mapper.ResponseMapper;
import org.example.pool.BookingDetailsPool;
import org.example.steps.BookerClientSteps;
import org.example.tags.Regression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Regression
@SpringBootTest(classes = SpringConfig.class)
public class SpringTestContext {

  @Autowired protected BookerClient bookerClient;

  @Autowired protected BookerClientSteps bookerClientSteps;

  @Autowired protected BookingDetailsPool bookingDetailsPool;

  @Autowired protected ResponseMapper responseMapper;

  // assertions
  @Autowired protected TokenResponseAssertion tokenResponseAssertion;

  @Autowired protected BookingAssertion bookingAssertion;

  @Autowired protected BookingDetailsAssertion bookingDetailsAssertion;

  @Autowired protected BookingIdAssertion bookingIdAssertion;

  @Autowired protected StringResponseAssertion stringResponseAssertion;
}
