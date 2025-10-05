package org.example.config;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:config-${env}.properties",
    "classpath:config-qa.properties",
    "system:properties",
    "system:env"})
public interface ServerConfig extends Config {

  @Key("base_url")
  String baseUrl();

  @Key("username")
  String username();

  @Key("password")
  String password();

}
