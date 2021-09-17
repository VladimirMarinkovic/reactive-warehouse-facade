package com.example.reactivewarehousefacade.controller;

import com.example.reactivewarehousefacade.dto.response.InventoryItemResponse;
import com.example.reactivewarehousefacade.dto.response.InventoryPageResponse;
import com.example.reactivewarehousefacade.service.InventoryService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;


@RequestMapping(path = "/api/inventory")
@Slf4j
@RestController
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;


    @GetMapping(path = "/{warehouseId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully returned inventory."),
            @ApiResponse(code = 400, message = "The received request is not syntactically valid. Correct the request and try again.")
    })
    public Flux<InventoryPageResponse> getWarehouseFluxedInventory(@PathVariable @NotBlank final String warehouseId) {
        log.debug("Getting fluxed Inventory for warehouseID: {}.", warehouseId);
        return this.inventoryService.getWarehouseFluxedInventory(warehouseId);
    }


    @GetMapping(path = "/{warehouseId}/item/{itemId}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully returned inventory item."),
            @ApiResponse(code = 400, message = "The received request is not syntactically valid. Correct the request and try again.")
    })
    public Mono<InventoryItemResponse> getWarehouseInventoryItem(@PathVariable @NotBlank final String warehouseId,
                                                                 @PathVariable @NotBlank final String itemId) {

        log.debug("Get Inventory Item for warehouseID: {} and itemId {}", warehouseId, itemId);
        return this.inventoryService.getWarehouseInventoryItem(warehouseId, itemId);
    }

}
