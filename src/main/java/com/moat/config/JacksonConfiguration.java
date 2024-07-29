package com.moat.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfiguration {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer addCustomBigDecimalDeserialization() {
    return new Jackson2ObjectMapperBuilderCustomizer() {
      @Override
      public void customize(
          Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        jacksonObjectMapperBuilder.featuresToDisable(
            DeserializationFeature.ACCEPT_FLOAT_AS_INT);
      }
    };
  }
}

