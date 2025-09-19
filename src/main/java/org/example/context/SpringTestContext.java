package org.example.context;

import org.example.client.BookerClient;
import org.example.config.AppConfig;
import org.example.factory.UserRequestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AppConfig.class)
public class SpringTestContext {

  @SuppressWarnings("unused")
  @Autowired
  protected AppConfig appConfig;

  @Autowired
  protected BookerClient bookerClient;

  @Autowired
  protected UserRequestFactory userRequestFactory;

}
