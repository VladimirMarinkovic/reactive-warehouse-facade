package com.example.reactivewarehousefacade.service;

import com.example.reactivewarehousefacade.dto.request.InventoryItemUpdateRequest;
import com.example.reactivewarehousefacade.dto.response.InventoryUpdatedItemResponse;
import reactor.core.publisher.Flux;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface InventoryUpdateService {

    void handleInventoryItemUpdate(@NotBlank final String warehouseId, @NotBlank final String itemId, @NotNull final InventoryItemUpdateRequest updateRequest);

    Flux<InventoryUpdatedItemResponse> getInventoryUpdatedItemSink();

}
