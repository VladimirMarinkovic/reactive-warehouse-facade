package com.example.reactivewarehousefacade.integration;

import com.example.reactivewarehousefacade.configuration.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${feign.inventory-details.name:inventoryDetails}", url = "${feign.inventory-details.url:http://localhost:8081}", configuration = FeignConfiguration.class)
public interface InventoryControllerApiClient extends InventoryControllerApi {
}
