package com.example.reactivewarehousefacade.service.impl;

import com.example.reactivewarehousefacade.dto.request.InventoryItemUpdateRequest;
import com.example.reactivewarehousefacade.dto.response.InventoryUpdatedItemResponse;
import com.example.reactivewarehousefacade.integration.InventoryControllerApiClient;
import com.example.reactivewarehousefacade.integration.dto.InventoryUpdatedItemResponseDTO;
import com.example.reactivewarehousefacade.service.InventoryUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import javax.validation.constraints.NotNull;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryUpdateServiceImpl implements InventoryUpdateService {


    private final InventoryControllerApiClient inventoryControllerApiClient;

    private final Sinks.Many<InventoryUpdatedItemResponse> inventoryItemUpdatedSink = Sinks.many().multicast().onBackpressureBuffer();


    @Override
    public void handleInventoryItemUpdate(final String warehouseId,
                                          final String itemId, final InventoryItemUpdateRequest updateRequest) {
        log.debug("Handling Inventory Item updating for warehouseID: {} and itemID: {}", warehouseId, itemId);

        var updatedInventoryItem = this.updateWarehouseInventoryItem(warehouseId, itemId, updateRequest);
        log.debug("Inventory Updated Item: {}", updatedInventoryItem);

        var result = inventoryItemUpdatedSink.tryEmitNext(InventoryUpdatedItemResponse.builder()
                                                                                                    .warehouseId(updatedInventoryItem.getWarehouseId())
                                                                                                    .id(updatedInventoryItem.getItemId())
                                                                                                    .name(updatedInventoryItem.getItemName())
                                                                                                    .type(updatedInventoryItem.getItemType())
                                                                                                    .build());
        if (!Sinks.EmitResult.OK.equals(result)) {
            log.error("Failed to update Inventory Item: {}", updatedInventoryItem);
        }

    }


    @Override
    public Flux<InventoryUpdatedItemResponse> getInventoryUpdatedItemSink() {
        return this.inventoryItemUpdatedSink.asFlux();
    }




    private @NotNull InventoryUpdatedItemResponseDTO updateWarehouseInventoryItem(final String warehouseId,
                                                                                  final String itemId, final InventoryItemUpdateRequest updateRequest) {

        return ofNullable(inventoryControllerApiClient.updateWarehouseInventoryItem(warehouseId, itemId, updateRequest))
                .filter(res -> res.getStatusCode().is2xxSuccessful())
                .filter(res -> nonNull(res.getBody()))
                .map(ResponseEntity::getBody)
                .orElseThrow(() -> new IllegalArgumentException(format("Failed to update warehouse inventory item for warehouseID: %s and itemID %s", warehouseId, itemId)));
    }

}
