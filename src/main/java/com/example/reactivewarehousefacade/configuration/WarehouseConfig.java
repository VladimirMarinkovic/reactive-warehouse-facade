package com.example.reactivewarehousefacade.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
public class WarehouseConfig {

    @Value("${warehouse.inventory.page-size:5}")
    private Integer pageSize;

}
