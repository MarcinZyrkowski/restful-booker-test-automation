package org.example.context;

import org.example.client.BookerClient;
import org.example.config.SpringConfig;
import org.example.pool.BookingDetailsPool;
import org.example.steps.BookerClientSteps;
import org.example.tags.Regression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Regression
@SpringBootTest(classes = SpringConfig.class)
public class SpringTestContext {

  @Autowired
  protected BookerClient bookerClient;

  @Autowired
  protected BookerClientSteps bookerClientSteps;

  @Autowired
  protected BookingDetailsPool bookingDetailsPool;

}
