package org.example;

import org.example.client.BookerClient;
import org.example.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Config.class)
public class SpringContext {

  @Autowired
  protected Config config;

  @Autowired
  protected BookerClient bookerClient;

}
