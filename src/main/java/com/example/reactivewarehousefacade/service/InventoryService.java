package com.example.reactivewarehousefacade.service;

import com.example.reactivewarehousefacade.dto.InventoryItemsCountDTO;
import com.example.reactivewarehousefacade.dto.response.InventoryItemResponse;
import com.example.reactivewarehousefacade.dto.response.InventoryPageResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;

public interface InventoryService {

    Flux<InventoryPageResponse> getWarehouseFluxedInventory(@NotBlank final String warehouseId);

    Mono<InventoryItemResponse> getWarehouseInventoryItem(@NotBlank final String warehouseId, @NotBlank final String itemId);

    InventoryItemsCountDTO getWarehouseInventoryItemsCount(@NotBlank final String warehouseId);

}
