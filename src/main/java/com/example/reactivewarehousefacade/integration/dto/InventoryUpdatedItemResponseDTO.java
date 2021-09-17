package com.example.reactivewarehousefacade.integration.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel("Inventory Updated Item Response")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryUpdatedItemResponseDTO implements Serializable {


    private static final long serialVersionUID = -7703750955400959507L;

    @ApiModelProperty(position = 1, value = "Warehouse ID", required = true, example = "34a4eeed-c409-45b8-a16d-0af87f14173c")
    private String warehouseId;

    @ApiModelProperty(position = 2, value = "Inventory Item ID", required = true, example = "34a4eeed-c409-45b8-a16d-0af87f14173c")
    private String itemId;

    @ApiModelProperty(position = 3, value = "Inventory Item Name", required = true, example = "example-name")
    private String itemName;

    @ApiModelProperty(position = 4, value = "Inventory Item Type", required = true, example = "example-type")
    private String itemType;


}
