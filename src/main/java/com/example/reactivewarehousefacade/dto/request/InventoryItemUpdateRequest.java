package com.example.reactivewarehousefacade.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel("Inventory Item Update Request")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryItemUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1040281086016282319L;

    @ApiModelProperty(position = 2, value = "Inventory Item Name", required = true, example = "example-name")
    private String name;

    @ApiModelProperty(position = 3, value = "Inventory Item Type", required = true, example = "example-type")
    private String type;


}
