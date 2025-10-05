package org.example.context;

import org.example.client.BookerClient;
import org.example.config.SpringConfig;
import org.example.steps.BookerClientSteps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SpringConfig.class)
public class SpringTestContext {

  @Autowired
  protected BookerClient bookerClient;

  @Autowired
  protected BookerClientSteps bookerClientSteps;

}
