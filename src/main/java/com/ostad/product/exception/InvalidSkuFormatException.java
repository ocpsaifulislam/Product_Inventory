package com.ostad.product.exception;

public class InvalidSkuFormatException extends RuntimeException {
    public InvalidSkuFormatException(String sku) {
        super("Invalid SKU format: '" + sku + "'. SKU must start with 'SKU-' followed by 8 alphanumeric characters (e.g., SKU-A1B2C3D4).");
    }
}
