package com.example.reactivewarehousefacade.integration.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel("Warehouse Inventory Count")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemsCountResponseDTO implements Serializable {


    @ApiModelProperty("The Warehouse Inventory Identifier.")
    private String warehouseId;

    @ApiModelProperty("The Warehouse Inventory Total Elements available.")
    private Long totalElements ;

}
