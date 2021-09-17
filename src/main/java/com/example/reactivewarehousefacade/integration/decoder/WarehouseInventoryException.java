package com.example.reactivewarehousefacade.integration.decoder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class WarehouseInventoryException extends RuntimeException {

    private static final long serialVersionUID = -4027843976829797112L;

    @Getter
    protected int returnCode;

    @Getter
    protected String errorCause;

    public WarehouseInventoryException(int returnCode, String errorCause) {
        this.returnCode = returnCode;
        this.errorCause = errorCause;
        log.error("Error Code:{} and Error Cause:{}", returnCode, errorCause);
    }
}
