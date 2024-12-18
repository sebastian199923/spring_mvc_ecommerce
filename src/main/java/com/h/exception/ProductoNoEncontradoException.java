package com.h.exception;

public class ProductoNoEncontradoException extends RuntimeException {
    public ProductoNoEncontradoException() {
        super("Producto no encontrado");
    }
}