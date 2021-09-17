package com.example.reactivewarehousefacade.controller;

import com.example.reactivewarehousefacade.dto.request.InventoryItemUpdateRequest;
import com.example.reactivewarehousefacade.dto.response.InventoryUpdatedItemResponse;
import com.example.reactivewarehousefacade.service.InventoryUpdateService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;


@RequestMapping(path = "/api/inventory")
@Slf4j
@RestController
@RequiredArgsConstructor
public class InventoryUpdateController {

    private final InventoryUpdateService inventoryUpdateService;


    @PutMapping(path = "/{warehouseId}/item/{itemId}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated inventory item."),
            @ApiResponse(code = 400, message = "The received request is not syntactically valid. Correct the request and try again.")
    })
    public ResponseEntity<Void> updateInventoryItem(@PathVariable @NotBlank final String warehouseId,
                                                    @PathVariable @NotBlank final String itemId,
                                                    @RequestBody @Valid final InventoryItemUpdateRequest updateRequest) {

        log.debug("Updating Inventory Item for warehouseID: {} and itemId {}", warehouseId, itemId);
        this.inventoryUpdateService.handleInventoryItemUpdate(warehouseId, itemId, updateRequest);
        return ResponseEntity.ok().build();
    }


    @GetMapping(value = "/item/updatable", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully returned inventory updated item."),
            @ApiResponse(code = 400, message = "The received request is not syntactically valid. Correct the request and try again.")
    })
    public Flux<InventoryUpdatedItemResponse> getInventoryUpdatedItem() {
        return this.inventoryUpdateService.getInventoryUpdatedItemSink();
    }
}
