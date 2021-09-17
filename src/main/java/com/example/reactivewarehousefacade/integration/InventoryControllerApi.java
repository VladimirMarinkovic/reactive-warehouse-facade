package com.example.reactivewarehousefacade.integration;

import com.example.reactivewarehousefacade.dto.request.InventoryItemUpdateRequest;
import com.example.reactivewarehousefacade.integration.dto.InventoryUpdatedItemResponseDTO;
import com.example.reactivewarehousefacade.integration.dto.InventoryItemResponseDTO;
import com.example.reactivewarehousefacade.integration.dto.InventoryPageResponseDTO;
import com.example.reactivewarehousefacade.integration.dto.InventoryItemsCountResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;


public interface InventoryControllerApi {

    @GetMapping(value = "/inventory/{warehouseId}/count")
    ResponseEntity<InventoryItemsCountResponseDTO> getWarehouseInventoryItemsCount(@PathVariable("warehouseId") @NotBlank  final String warehouseId);

    @GetMapping(value = "/inventory/{warehouseId}")
    ResponseEntity<InventoryPageResponseDTO> getWarehouseInventoryPage(@PathVariable("warehouseId") @NotBlank  final String warehouseId,
                                                                       @RequestParam(value = "pageNumber", required = false, defaultValue = "0") final Integer pageNumber,
                                                                       @RequestParam(value = "pageSize", required = false, defaultValue = "5") final Integer pageSize);

    @GetMapping(value = "/inventory/{warehouseId}/item/{itemId}")
    ResponseEntity<InventoryItemResponseDTO> getWarehouseInventoryItem(@PathVariable("warehouseId") @NotBlank  final String warehouseId,
                                                                       @PathVariable("itemId") @NotBlank  final String itemId);

    @PutMapping(value = "/inventory/{warehouseId}/item/{itemId}")
    ResponseEntity<InventoryUpdatedItemResponseDTO> updateWarehouseInventoryItem(@PathVariable("warehouseId") @NotBlank  final String warehouseId,
                                                                                 @PathVariable("itemId") @NotBlank  final String itemId,
                                                                                 @RequestBody @Valid InventoryItemUpdateRequest updateRequest);

}
