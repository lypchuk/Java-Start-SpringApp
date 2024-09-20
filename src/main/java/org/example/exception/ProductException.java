package org.example.exception;

import java.io.Serial;

public class ProductException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;
    public ProductException() {
        super();
    }
    public ProductException(String customMessage) {
        super(customMessage);
    }
}
