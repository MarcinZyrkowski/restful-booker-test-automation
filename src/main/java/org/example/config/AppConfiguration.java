package org.example.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.aeonbits.owner.ConfigFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConfiguration {

  public static final ServerConfig CONFIG = ConfigFactory.create(ServerConfig.class);
}
