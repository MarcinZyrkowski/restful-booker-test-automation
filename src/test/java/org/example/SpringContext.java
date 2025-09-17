package org.example;

import org.example.client.BookerClient;
import org.example.config.Config;
import org.example.factory.UserRequestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Config.class)
public class SpringContext {

  @SuppressWarnings("unused")
  @Autowired
  protected Config config;

  @Autowired
  protected BookerClient bookerClient;

  @Autowired
  protected UserRequestFactory userRequestFactory;

}
