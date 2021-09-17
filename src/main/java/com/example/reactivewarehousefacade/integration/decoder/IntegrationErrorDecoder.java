package com.example.reactivewarehousefacade.integration.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class IntegrationErrorDecoder implements ErrorDecoder {


    private final ObjectMapper objectMapper;


    @Override
    public Exception decode(String methodCalled, Response response) {

        final String[] clientCall = methodCalled.split("#");
        final String clientName = clientCall[0];
        final String clientMethod = clientCall[1];

        switch (clientName) {
            case "InventoryControllerApiClient":
                return this.decodeInventoryControllerApiClient(clientMethod, response);
            default:
                return new RuntimeException("Something went wrong");
        }
    }


    private Exception decodeInventoryControllerApiClient(String methodCalled, Response response) {
        final String parsedErrorMessage = this.parseErrorMessage(response);
        if (methodCalled.startsWith("getWarehouseInventoryItemsCount")) {
            return new WarehouseInventoryException(response.status(), parsedErrorMessage);

        } else if (methodCalled.startsWith("getWarehouseInventoryPage")) {
            return new WarehouseInventoryException(response.status(), parsedErrorMessage);

        } else if (methodCalled.startsWith("getWarehouseInventoryItem")) {
            return new WarehouseInventoryException(response.status(), parsedErrorMessage);

        } else if (methodCalled.startsWith("updateWarehouseInventoryItem")) {
            return new WarehouseInventoryException(response.status(), parsedErrorMessage);
        }

        return new RuntimeException("Something went wrong with Warehouse Inventory.");
    }



    private String parseErrorMessage(Response response) {
        try {
            final String stringResponseBody =  IOUtils.toString(response.body().asInputStream());
            return objectMapper.readTree(stringResponseBody).findValue("message").asText();
        } catch (IOException ignored) {
            log.error("Error while parsing error message from response.");
            return "";
        }
    }
}
