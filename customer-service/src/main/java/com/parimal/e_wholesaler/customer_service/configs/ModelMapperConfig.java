package com.parimal.e_wholesaler.customer_service.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ModelMapperConfig {
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
