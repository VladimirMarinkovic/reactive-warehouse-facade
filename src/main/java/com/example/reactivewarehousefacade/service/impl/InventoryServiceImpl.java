package com.example.reactivewarehousefacade.service.impl;

import com.example.reactivewarehousefacade.configuration.WarehouseConfig;
import com.example.reactivewarehousefacade.dto.response.InventoryItemResponse;
import com.example.reactivewarehousefacade.dto.response.InventoryPageResponse;
import com.example.reactivewarehousefacade.dto.response.InventoryUpdatedItemResponse;
import com.example.reactivewarehousefacade.dto.InventoryItemDTO;
import com.example.reactivewarehousefacade.dto.InventoryItemsCountDTO;
import com.example.reactivewarehousefacade.integration.InventoryControllerApiClient;
import com.example.reactivewarehousefacade.integration.dto.InventoryPageItemDTO;
import com.example.reactivewarehousefacade.integration.dto.InventoryPageResponseDTO;
import com.example.reactivewarehousefacade.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final WarehouseConfig warehouseConfig;
    private final InventoryControllerApiClient inventoryControllerApiClient;


    @Override
    public Flux<InventoryPageResponse> getWarehouseFluxedInventory(final String warehouseId) {
        log.debug("Getting Inventory pages for warehouseID: {}", warehouseId);

        log.debug("Getting Inventory Items count.");
        var inventoryItemsCount = this.getWarehouseInventoryItemsCount(warehouseId);
        log.debug("Total Items available: {} and Page Size set to: {}", inventoryItemsCount, warehouseConfig.getPageSize());

        var totalNumberOfPages = this.calculateTotalNumberOfPages(inventoryItemsCount);

        return Flux.range(1, totalNumberOfPages)
                   .map(pageNumber -> {

                       log.debug("Getting page {} of total pages available: {}", pageNumber, totalNumberOfPages);
                       var currentPageInventory = this.getWarehouseInventoryPage(
                               warehouseId,
                               pageNumber,
                               warehouseConfig.getPageSize()
                       );
                       var mappedPageInventoryItems = this.mapInventoryItems(currentPageInventory.getItems());

                       return InventoryPageResponse.builder()
                                                   .pageNumber(pageNumber)
                                                   .totalPages(totalNumberOfPages)
                                                   .totalElements(mappedPageInventoryItems.size())
                                                   .items(mappedPageInventoryItems)
                                                   .build();
                   });
    }

    @Override
    public Mono<InventoryItemResponse> getWarehouseInventoryItem(final String warehouseId, final String itemId) {
        return Mono.just(
                ofNullable(inventoryControllerApiClient.getWarehouseInventoryItem(warehouseId, itemId))
                        .filter(res -> res.getStatusCode().is2xxSuccessful())
                        .filter(res -> nonNull(res.getBody()))
                        .map(ResponseEntity::getBody)
                        .map(inventoryItem ->  InventoryItemResponse.builder()
                                                                    .warehouseId(inventoryItem.getWarehouseId())
                                                                    .id(inventoryItem.getItemId())
                                                                    .name(inventoryItem.getItemName())
                                                                    .type(inventoryItem.getItemType())
                                                                    .build())
                        .orElseThrow(() -> new IllegalArgumentException(format("Failed to retrieve warehouse inventory item for warehouseID: %s and itemId %s ", warehouseId, itemId)))
        );
    }


    @Override
    public @NotNull InventoryItemsCountDTO getWarehouseInventoryItemsCount(final String warehouseId) {
        return ofNullable(inventoryControllerApiClient.getWarehouseInventoryItemsCount(warehouseId))
                .filter(res -> res.getStatusCode().is2xxSuccessful())
                .filter(res -> nonNull(res.getBody()))
                .map(ResponseEntity::getBody)
                .map(inventoryCount ->  InventoryItemsCountDTO.builder()
                                                              .warehouseId(inventoryCount.getWarehouseId())
                                                              .totalElements(inventoryCount.getTotalElements())
                                                              .build())
                .orElseThrow(() -> new IllegalArgumentException(format("Failed to retrieve warehouse inventory items count for warehouseID: %s ", warehouseId)));
    }



    /**
     * Represents the method responsible to calculate number of pages based on inventoryItemsCount response and configured page size.
     * @param inventoryItemsCount The Inventory`s items count ID for which to get all subscriptions offers.
     * @return The calculated number of pages.
     */
    private @NotNull Integer calculateTotalNumberOfPages(final InventoryItemsCountDTO inventoryItemsCount) {
        var totalElements = inventoryItemsCount.getTotalElements();
        return totalElements == 0 ? 1
                                  : (int) (Math.ceil(totalElements / Float.valueOf(warehouseConfig.getPageSize())));
    }


    private @NotNull InventoryPageResponseDTO getWarehouseInventoryPage(final String warehouseId, final Integer pageNumber, final Integer pageSize) {
        return ofNullable(inventoryControllerApiClient.getWarehouseInventoryPage(warehouseId, pageNumber, pageSize))
                .filter(res -> res.getStatusCode().is2xxSuccessful())
                .filter(res -> nonNull(res.getBody()))
                .map(ResponseEntity::getBody)
                .orElseThrow(() -> new IllegalArgumentException(format("Failed to retrieve warehouse inventory items count for warehouseID: %s ", warehouseId)));
    }


    private @NotNull List<InventoryItemDTO> mapInventoryItems(final List<InventoryPageItemDTO> inventoryPageItems) {
        return inventoryPageItems.stream()
                                 .map(inventoryPageItem -> InventoryItemDTO.builder()
                                                                           .id(inventoryPageItem.getId())
                                                                           .name(inventoryPageItem.getName())
                                                                           .type(inventoryPageItem.getType())
                                                                           .build())
                                 .collect(Collectors.toList());
    }

}
