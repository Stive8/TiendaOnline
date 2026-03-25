package com.bsortegon.tienda.tiendacamisetas.exception;

public class InsufficientStockException extends RuntimeException {
    private final Long availableStock;
    private final Integer requestedQuantity;

    public InsufficientStockException(String productName, Long availableStock, Integer requestedQuantity) {
        super(String.format("Insufficient stock for %s. Available: %d, Requested: %d", 
                productName, availableStock, requestedQuantity));
        this.availableStock = availableStock;
        this.requestedQuantity = requestedQuantity;
    }

    public Long getAvailableStock() {
        return availableStock;
    }

    public Integer getRequestedQuantity() {
        return requestedQuantity;
    }
}
