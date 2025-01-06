package com.parimal.e_wholesaler.product_service.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

}
