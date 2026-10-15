package com.alura.reserva_de_salas_api.exception;

public class CapacidadeImcompativelException extends RuntimeException {
    public CapacidadeImcompativelException(String message) {
        super(message);
    }
}
