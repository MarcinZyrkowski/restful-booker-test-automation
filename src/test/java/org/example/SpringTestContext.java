package org.example;

import org.example.assertionNG.auth.TokenResponseAssertion;
import org.example.assertionNG.booking.BookingAssertion;
import org.example.assertionNG.booking.BookingDetailsAssertion;
import org.example.assertionNG.booking.BookingIdAssertion;
import org.example.assertionNG.common.ErrorResponseAssertion;
import org.example.assertionNG.common.StringResponseAssertion;
import org.example.client.BookerClient;
import org.example.config.SpringConfig;
import org.example.dataprovider.BookingDataProvider;
import org.example.factory.auth.UserFactory;
import org.example.factory.booking.BookingFactory;
import org.example.mapper.DateMapper;
import org.example.model.dto.request.auth.User;
import org.example.pool.BookingDetailsPool;
import org.example.steps.BookerClientSteps;
import org.example.tags.Regression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Regression
@SpringBootTest(classes = SpringConfig.class)
public class SpringTestContext {

  @Autowired protected User adminUser;

  @Autowired protected BookerClient bookerClient;

  @Autowired protected BookerClientSteps bookerClientSteps;

  @Autowired protected BookingDetailsPool bookingDetailsPool;

  // factory
  @Autowired protected UserFactory userFactory;
  @Autowired protected BookingFactory bookingFactory;

  @Autowired protected BookingDataProvider bookingDataProvider;

  @Autowired protected DateMapper dateMapper;

  // assertions
  @Autowired protected ErrorResponseAssertion errorResponseAssertion;
  @Autowired protected BookingAssertion bookingAssertion;
  @Autowired protected BookingDetailsAssertion bookingDetailsAssertion;
  @Autowired protected BookingIdAssertion bookingIdAssertion;
  @Autowired protected StringResponseAssertion stringResponseAssertion;
  @Autowired protected TokenResponseAssertion tokenResponseAssertion;
}
