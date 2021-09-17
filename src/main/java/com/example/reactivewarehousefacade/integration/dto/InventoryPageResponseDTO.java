package com.example.reactivewarehousefacade.integration.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel("Inventory Page Response")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryPageResponseDTO implements Serializable {

    private static final long serialVersionUID = -1181685403685051800L;

    @ApiModelProperty("The Inventory Page Number.")
    private Integer pageNumber;

    @ApiModelProperty("The Inventory Total Elements available.")
    private Long totalElements ;

    @ApiModelProperty("The Inventory Total Pages available.")
    private Integer totalPages;

    @ApiModelProperty("Inventory Items.")
    private List<InventoryPageItemDTO> items;

}
