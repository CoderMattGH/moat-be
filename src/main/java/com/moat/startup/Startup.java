package com.moat.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Class to hold startup logic.  The afterPropertiesSet() method will be executed after Spring
 * Boot has been initialised.
 */
@Component
public class Startup implements InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(Startup.class);

  @Override
  public void afterPropertiesSet() throws Exception {
    // Startup logic goes here.
  }
}
