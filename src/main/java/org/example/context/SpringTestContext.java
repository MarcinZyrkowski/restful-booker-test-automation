package org.example.context;

import org.example.client.BookerClient;
import org.example.config.AppConfig;
import org.example.factory.auth.UserRequestFactory;
import org.example.factory.booking.CreateBookingRequestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AppConfig.class)
public class SpringTestContext {

  @Autowired
  protected BookerClient bookerClient;

  @Autowired
  protected UserRequestFactory userRequestFactory;

  @Autowired
  protected CreateBookingRequestFactory createBookingRequestFactory;

}
