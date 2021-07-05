package io.verticle.kubernetes.authgateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper o = new ObjectMapper();

        return o;
    }
}
